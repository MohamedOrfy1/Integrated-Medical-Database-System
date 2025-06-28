import React, { useState, useEffect } from 'react';
import { HematologyService } from '../services/HematologyService';
import '../styles/Hematology.css';

function parseHematologyReport(htmlString, testAttributes) {
    const parser = new window.DOMParser();
    const doc = parser.parseFromString(htmlString, 'text/html');

    console.log("All font labels:");
    Array.from(doc.querySelectorAll('font')).forEach(el => {
        console.log(JSON.stringify(el.textContent));
    });

    function getValueAfterLabelFont(label) {
        const normLabel = label.replace(/[:\s]+/g, ' ').trim().toLowerCase();
        const font = Array.from(doc.querySelectorAll('font')).find(el => {
            const b = el.querySelector('b');
            if (!b) return false;
            const txt = b.textContent.replace(/[:\s]+/g, ' ').trim().toLowerCase();
            return txt.includes(normLabel);
        });
        if (font) {
            let td = font.closest('td');
            if (td) {
                let tr = td.closest('tr');
                if (tr) {
                    const tds = Array.from(tr.querySelectorAll('td'));
                    const idx = tds.indexOf(td);
                    // Find the next non-empty <td> after the label <td>
                    for (let i = idx + 1; i < tds.length; i++) {
                        const valueTd = tds[i];
                        const tt = valueTd.querySelector('tt');
                        if (tt && tt.textContent.trim()) return tt.textContent.trim();
                        const fontVal = valueTd.querySelector('font');
                        if (fontVal && fontVal.textContent.trim()) return fontVal.textContent.trim();
                        if (valueTd.textContent.trim()) return valueTd.textContent.trim();
                    }
                }
            }
        }
        return '';
    }

    // Helper to normalize test names for matching
    function normalizeTestName(name) {
        return name.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
    }

    // Parse CBC test values
    function parseTestValues(doc) {
        const tests = [];
        const metaMap = {};
        testAttributes.forEach(meta => {
            metaMap[normalizeTestName(meta.attributeName)] = meta;
        });

        // Debug: log all normalized test attribute names
        console.log('Test attributes normalized names:', Object.keys(metaMap));

        Array.from(doc.querySelectorAll('font')).forEach(font => {
            const rawName = font.textContent.trim();
            const normName = normalizeTestName(rawName);
            console.log('HTML test candidate:', rawName, '->', normName);
            if (metaMap[normName]) {
                let td = font.closest('td');
                if (td) {
                    let tr = td.closest('tr');
                    if (tr) {
                        const tds = Array.from(tr.querySelectorAll('td'));
                        const idx = tds.indexOf(td);
                        // Find the next <td> with a numeric value (the result)
                        for (let i = idx + 1; i < tds.length; i++) {
                            const valueTd = tds[i];
                            const tt = valueTd.querySelector('tt');
                            let value = '';
                            if (tt && tt.textContent.trim()) value = tt.textContent.trim();
                            else if (valueTd.textContent.trim()) value = valueTd.textContent.trim();
                            if (/^-?\d+(\.\d+)?$/.test(value.replace(/,/g, ''))) {
                                const numValue = parseFloat(value.replace(/,/g, ''));
                                let flag = '';
                                if (!isNaN(numValue)) {
                                    if (typeof metaMap[normName].fromRange === 'number' && typeof metaMap[normName].toRange === 'number') {
                                        if (numValue < metaMap[normName].fromRange || numValue > metaMap[normName].toRange) {
                                            flag = 'abnormal';
                                        } else {
                                            flag = 'normal';
                                        }
                                    }
                                }
                                tests.push({
                                    test_name: metaMap[normName].attributeName,
                                    result: value,
                                    unit: metaMap[normName].unit,
                                    reference_range: `${metaMap[normName].fromRange}-${metaMap[normName].toRange}`,
                                    flag
                                });
                                break;
                            }
                        }
                    }
                }
            }
        });
        return tests;
    }

    // 1. Lab No. / ID
    let labNo = getValueAfterLabelFont('Lab No.');

    // 2. Request Date / Test Date
    let requestDate = getValueAfterLabelFont('Request Date');
    let sample_date = '';
    if (requestDate) {
        const match = requestDate.match(/(\d{2})-(\d{2})-(\d{4})/);
        if (match) sample_date = `${match[3]}-${match[2]}-${match[1]}`;
        else sample_date = requestDate;
    }

    // 3. Patient Name
    let patientName = getValueAfterLabelFont('Patient Name');
    console.log("Patient Name:", patientName);
    // 4. Age
    let age = getValueAfterLabelFont('Age');
    if (age) {
        const match = age.match(/(\d+)/);
        if (match) age = match[1];
    }

    // 5. Sex
    let sex = getValueAfterLabelFont('Sex');
    if (sex) {
        if (/male/i.test(sex)) sex = 'Male';
        else if (/female/i.test(sex)) sex = 'Female';
    }

    // 6. Referred by / Dr
    let referredBy = getValueAfterLabelFont('Referred by');
    if (!referredBy) referredBy = getValueAfterLabelFont('Dr');

    // Debug log
    console.log({ patientName, age, sex, labNo, referredBy, requestDate });

    // Parse CBC test values
    const parsedTests = parseTestValues(doc);
    console.log(parsedTests);

    // Parse comments (robust: search forward for first non-empty <font> after label)
    let report_comments = '';
    const allFonts = Array.from(doc.querySelectorAll('font'));
    const commentsIdx = allFonts.findIndex(el => {
        const b = el.querySelector('b');
        if (!b) return false;
        const txt = b.textContent.replace(/[:\s]+/g, ' ').trim().toLowerCase();
        return txt.includes('comments');
    });
    if (commentsIdx !== -1) {
        for (let i = commentsIdx + 1; i < allFonts.length; i++) {
            const txt = allFonts[i].textContent.trim();
            if (txt && !/comments/i.test(txt)) {
                report_comments = txt;
                break;
            }
        }
    }
    console.log('Report comments:', report_comments);
    return {
        patient_info: {
            name: patientName || '',
            age: age || '',
            gender: sex || '',
            test_id: labNo || ''
        },
        test_details: {
            sample_date: sample_date || '',
            print_date: new Date().toISOString().split('T')[0],
            referring_physician: referredBy || ''
        },
        blood_count_report: {
            tests: parsedTests.length > 0 ? parsedTests : [
                { test_name: '', result: '', unit: '', reference_range: '', flag: '' }
            ],
            report_comments: report_comments || ''
        }
    };
}

const Hematology = () => {
    const [activeTab, setActiveTab] = useState('manual');
    const [testAttributes, setTestAttributes] = useState([]);
    const [loadingAttributes, setLoadingAttributes] = useState(true);
    const [formData, setFormData] = useState({
        patient_info: {
            name: '',
            age: '',
            gender: '',
            test_id: ''
        },
        test_details: {
            sample_date: '',
            referring_physician: ''
        },
        blood_count_report: {
            tests: [
                { test_name: '', result: '', unit: '', reference_range: '', flag: '' }
            ],
            report_comments: ''
        }
    });
    const [doctorComments, setDoctorComments] = useState('');
    const [file, setFile] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [addTestLoading, setAddTestLoading] = useState(false);
    const [addTestSuccess, setAddTestSuccess] = useState('');
    const [doctorId, setDoctorId] = useState('');

    // Fetch test attributes from API
    const fetchTestAttributes = async () => {
        try {
            setLoadingAttributes(true);
            const attributes = await HematologyService.getTestAttributes();
            setTestAttributes(attributes);
            console.log('Fetched test attributes:', attributes);
        } catch (error) {
            console.error('Error fetching test attributes:', error);
            setError('Failed to load test attributes. Please refresh the page.');
        } finally {
            setLoadingAttributes(false);
        }
    };

    // Extract doctor ID from JWT token on component mount
    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                const base64Url = token.split('.')[1];
                const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
                const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
                    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                }).join(''));
                const decodedToken = JSON.parse(jsonPayload);
                setDoctorId(decodedToken.sub); // 'sub' is the standard JWT claim for subject (ID)
                console.log('Extracted doctor ID:', decodedToken.sub);
            } catch (error) {
                console.error('Error decoding JWT token:', error);
                setError('Error extracting doctor information from token');
            }
        }
        
        // Fetch test attributes
        fetchTestAttributes();
    }, []);

    const getTestMeta = (testName) => {
        return testAttributes.find(t => t.attributeName === testName);
    };

    const handleTestTypeChange = (index, testName) => {
        const meta = getTestMeta(testName);
        setFormData(prev => {
            const newTests = [...prev.blood_count_report.tests];
            newTests[index] = {
                ...newTests[index],
                test_name: testName,
                unit: meta ? meta.unit : '',
                reference_range: meta ? `${meta.fromRange}-${meta.toRange}` : '',
                flag: '',
                result: ''
            };
            return {
                ...prev,
                blood_count_report: {
                    ...prev.blood_count_report,
                    tests: newTests
                }
            };
        });
    };

    const handleResultChange = (index, value) => {
        setFormData(prev => {
            const newTests = [...prev.blood_count_report.tests];
            const test = newTests[index];
            const meta = getTestMeta(test.test_name);
            let flag = '';
            if (meta && value !== '') {
                const num = parseFloat(value);
                if (isNaN(num)) {
                    flag = '';
                } else if (num < meta.fromRange || num > meta.toRange) {
                    flag = 'abnormal';
                } else {
                    flag = 'normal';
                }
            }
            newTests[index] = {
                ...test,
                result: value,
                flag
            };
            return {
                ...prev,
                blood_count_report: {
                    ...prev.blood_count_report,
                    tests: newTests
                }
            };
        });
    };

    const handleInputChange = (section, field, value, index = null) => {
        setFormData(prev => {
            const newData = { ...prev };
            if (index !== null) {
                newData.blood_count_report.tests[index][field] = value;
            } else if (section === 'blood_count_report') {
                newData.blood_count_report[field] = value;
            } else {
                newData[section][field] = value;
            }
            return newData;
        });
    };

    const addTestRow = () => {
        setFormData(prev => ({
            ...prev,
            blood_count_report: {
                ...prev.blood_count_report,
                tests: [...prev.blood_count_report.tests, 
                    { test_name: '', result: '', unit: '', reference_range: '', flag: '' }
                ]
            }
        }));
    };

    const removeTestRow = (index) => {
        setFormData(prev => ({
            ...prev,
            blood_count_report: {
                ...prev.blood_count_report,
                tests: prev.blood_count_report.tests.filter((_, i) => i !== index)
            }
        }));
    };

    const handleFileUpload = async (event) => {
        const file = event.target.files[0];
        if (file) {
            setFile(file);
            try {
                const text = await file.text();
                const extractedData = parseHematologyReport(text, testAttributes);
                setFormData(extractedData);
                setDoctorComments(extractedData.blood_count_report.report_comments || '');
            } catch (error) {
                setError('Error parsing HTML file: ' + error.message);
            }
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const reportData = {
                ...formData,
                patient_info: {
                    ...formData.patient_info,
                    age: Number(formData.patient_info.age),
                },
                blood_count_report: {
                    ...formData.blood_count_report,
                    tests: formData.blood_count_report.tests.map(test => ({
                        ...test,
                        result: Number(test.result),
                    })),
                    report_comments: doctorComments
                }
            };

            console.log('Sending reportData:', reportData); // Debug log

            const pdfBlob = await HematologyService.generateReport(reportData);
            
            // Create a download link for the PDF
            const url = window.URL.createObjectURL(pdfBlob);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'hematology_report.pdf');
            document.body.appendChild(link);
            link.click();
            link.remove();
        } catch (error) {
            setError('Error generating report: ' + error.message);
        } finally {
            setLoading(false);
        }
    };

    const handleAddTest = async () => {
        setAddTestLoading(true);
        setError('');
        setAddTestSuccess('');

        try {
            // Format dates from YYYY-MM-DD to DD-MM-YYYY
            const formatDateForBackend = (dateString) => {
                if (!dateString) return '';
                const date = new Date(dateString);
                const day = String(date.getDate()).padStart(2, '0');
                const month = String(date.getMonth() + 1).padStart(2, '0');
                const year = date.getFullYear();
                return `${day}-${month}-${year}`;
            };

            // Format the data according to the backend expectations
            const testData = {
                test_details: {
                    patient_Id: formData.patient_info.test_id, // Using test_id as patient_Id
                    sample_date: formatDateForBackend(formData.test_details.sample_date),
                    print_date: formatDateForBackend(formData.test_details.print_date),
                    referring_physician_Id: doctorId || "28610151234561", // Use extracted doctor ID or fallback
                    asissting_physician_Id: doctorId || "28807081234563", // Use extracted doctor ID or fallback
                    comments: doctorComments || ""
                },
                CBC: {
                    tests: formData.blood_count_report.tests
                        .filter(test => test.test_name && test.result) // Only include tests with names and results
                        .map(test => ({
                            test_name: test.test_name,
                            result: test.result.toString()
                        }))
                }
            };

            console.log('Sending test data:', testData);

            const result = await HematologyService.insertTest(testData);
            
            if (result === true) {
                setAddTestSuccess('Test added successfully!');
                setTimeout(() => setAddTestSuccess(''), 3000);
            } else {
                setError('Failed to add test. Please try again.');
            }
        } catch (error) {
            console.error('Error adding test:', error);
            setError('Error adding test: ' + (error.response?.data || error.message));
        } finally {
            setAddTestLoading(false);
        }
    };

    return (
        <div className="hematology-container">
            <h1>Hematology Report</h1>
            
            <div className="tab-buttons">
                <button 
                    className={activeTab === 'manual' ? 'active' : ''} 
                    onClick={() => setActiveTab('manual')}
                >
                    Manual Input
                </button>
                <button 
                    className={activeTab === 'upload' ? 'active' : ''} 
                    onClick={() => setActiveTab('upload')}
                >
                    Upload HTML
                </button>
            </div>

            {error && <div className="error-message">{error}</div>}

            {activeTab === 'upload' ? (
                <div className="upload-section">
                    <input 
                        type="file" 
                        accept=".html,.htm" 
                        onChange={handleFileUpload}
                        className="file-input"
                    />
                    {file && <p>Selected file: {file.name}</p>}
                </div>
            ) : null}

            <form onSubmit={handleSubmit} className="hematology-form">
                <div className="form-section">
                    <h2>Patient Information</h2>
                    <div className="form-grid">
                        <input
                            type="text"
                            placeholder="Patient Name"
                            value={formData.patient_info.name}
                            onChange={(e) => handleInputChange('patient_info', 'name', e.target.value)}
                            required
                        />
                        <input
                            type="number"
                            placeholder="Age"
                            value={formData.patient_info.age}
                            onChange={(e) => handleInputChange('patient_info', 'age', e.target.value)}
                            required
                        />
                        <select
                            value={formData.patient_info.gender}
                            onChange={(e) => handleInputChange('patient_info', 'gender', e.target.value)}
                            required
                        >
                            <option value="">Select Gender</option>
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                        </select>
                        <input
                            type="text"
                            placeholder="Test ID"
                            value={formData.patient_info.test_id}
                            onChange={(e) => handleInputChange('patient_info', 'test_id', e.target.value)}
                            required
                        />
                    </div>
                </div>

                <div className="form-section">
                    <h2>Test Details</h2>
                    <div className="form-grid">
                        <input
                            type="date"
                            value={formData.test_details.sample_date}
                            onChange={(e) => handleInputChange('test_details', 'sample_date', e.target.value)}
                            required
                        />
                        <input
                            type="text"
                            placeholder="Referring Physician"
                            value={formData.test_details.referring_physician}
                            onChange={(e) => handleInputChange('test_details', 'referring_physician', e.target.value)}
                            required
                        />
                        <input
                            type="text"
                            placeholder="Doctor ID"
                            value={doctorId}
                            readOnly
                            style={{ backgroundColor: '#f5f5f5', cursor: 'not-allowed' }}
                        />
                    </div>
                </div>

                <div className="form-section">
                    <h2>Blood Count Tests</h2>
                    {loadingAttributes ? (
                        <div style={{ textAlign: 'center', padding: '20px' }}>
                            <p>Loading test attributes...</p>
                        </div>
                    ) : (
                        <div className="tests-table">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Test Name</th>
                                        <th>Result</th>
                                        <th>Unit</th>
                                        <th>Reference Range</th>
                                        <th>Flag</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {formData.blood_count_report.tests.map((test, index) => (
                                        <tr key={index}>
                                            <td>
                                                <select
                                                    value={test.test_name}
                                                    onChange={e => handleTestTypeChange(index, e.target.value)}
                                                    required
                                                >
                                                    <option value="">Select Test</option>
                                                    {testAttributes.map(t => (
                                                        <option key={t.attributeName} value={t.attributeName}>{t.attributeName}</option>
                                                    ))}
                                                </select>
                                            </td>
                                            <td>
                                                <input
                                                    type="number"
                                                    value={test.result}
                                                    onChange={e => handleResultChange(index, e.target.value)}
                                                    required
                                                    disabled={!test.test_name}
                                                />
                                            </td>
                                            <td>
                                                <input
                                                    type="text"
                                                    value={test.unit}
                                                    readOnly
                                                />
                                            </td>
                                            <td>
                                                <input
                                                    type="text"
                                                    value={test.reference_range}
                                                    readOnly
                                                />
                                            </td>
                                            <td>
                                                <input
                                                    type="text"
                                                    value={test.flag}
                                                    readOnly
                                                />
                                            </td>
                                            <td>
                                                <button 
                                                    type="button" 
                                                    onClick={() => removeTestRow(index)}
                                                    className="remove-button"
                                                >
                                                    Remove
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                            <button type="button" onClick={addTestRow} className="add-button">
                                Add Test
                            </button>
                        </div>
                    )}
                </div>

                <div className="form-section">
                    <h2>Doctor Comments</h2>
                    <textarea
                        value={doctorComments}
                        onChange={(e) => setDoctorComments(e.target.value)}
                        placeholder="Enter doctor's comments here..."
                        rows="4"
                        className="comments-textarea"
                    />
                </div>

                <div style={{ display: 'flex', gap: '16px', marginTop: '20px' }}>
                    <button type="submit" className="submit-button" disabled={loading}>
                        {loading ? 'Generating Report...' : 'Generate Report'}
                    </button>
                    <button 
                        type="button" 
                        onClick={handleAddTest} 
                        className="submit-button" 
                        disabled={addTestLoading}
                        style={{ backgroundColor: '#28a745' }}
                    >
                        {addTestLoading ? 'Adding Test...' : 'Add Test'}
                    </button>
                </div>
                {addTestSuccess && <div className="success-message" style={{ marginTop: '10px', color: '#28a745' }}>{addTestSuccess}</div>}
            </form>
        </div>
    );
};

export default Hematology; 