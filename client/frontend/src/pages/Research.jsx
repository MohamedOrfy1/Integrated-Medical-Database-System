import React, { useState, useEffect } from 'react';
import { Pie, Bar } from 'react-chartjs-2';
import { Chart as ChartJS, ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement } from 'chart.js';
ChartJS.register(ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement);
import '../styles/Research.css';
import { DoctorService } from '../services/DoctorService';

const Research = () => {

    const [patients, setPatients] = useState([]);
    const [filterPatient , setFilterPatient] = useState([]);
    const [diagnosisfilter, setDiagnosisfilter] = useState('');
    const [agefilter, setAgefilter] = useState('');
    const [genderfilter, setGenderfilter] = useState('');
    const [diagnoses, setDiagnoses] = useState([]);

    const [activeTab, setActiveTab] = useState('list');
    const [successMessage, setSuccessMessage] = useState('');
    const [error, setError] = useState('');

    // For statistics
    const [stats, setStats] = useState({ total: 0, byDiagnosis: {} ,byAgeRange: {} });

    const ageRanges = [
    { label: '1-10', min: 1, max: 10 },
    { label: '11-17', min: 11, max: 17 },
    { label: '18-20', min: 18, max: 20 },
    { label: '21-30', min: 21, max: 30 },
    { label: '31-40', min: 31, max: 40 },
    { label: '41-60', min: 41, max: 60 },
    { label: '>60', min: 61, max: Infinity },
  ];

    // Helper function to parse Egyptian National ID
    const parseEgyptianNationalId = (nationalId) => {
        const idString = String(nationalId);

        if (idString.length !== 14) {
            console.error("Invalid National ID length:", idString);
            return null;
        }

        // Century
        const centuryDigit = parseInt(idString.substring(0, 1));
        let yearPrefix;
        if (centuryDigit === 2) {
            yearPrefix = 1900;
        } else if (centuryDigit === 3) {
            yearPrefix = 2000;
        } else {
            console.error("Invalid century digit in National ID:", centuryDigit);
            return null;
        }

        // Birth Date (YYMMDD)
        const yearSuffix = parseInt(idString.substring(1, 3));
        const month = parseInt(idString.substring(3, 5));
        const day = parseInt(idString.substring(5, 7));

        const birthYear = yearPrefix + yearSuffix;

        // Calculate age
        const today = new Date();
        const birthDate = new Date(birthYear, month - 1, day); // Month is 0-indexed
        let age = today.getFullYear() - birthDate.getFullYear();
        const m = today.getMonth() - birthDate.getMonth();
        if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
            age--;
        }

        // Gender (13th digit from left, 2nd to last)
        const genderDigit = parseInt(idString.substring(12, 13));
        const gender = genderDigit % 2 === 1 ? 'Male' : 'Female';

        return { age, gender, birthYear, birthMonth: month, birthDay: day };
    };

    useEffect(() => {
        // Fetch diagnoses list
        const fetchDiagnoses = async () => {
            try {
                const diagnosisList = await DoctorService.getDiagnosisList();
                setDiagnoses(diagnosisList);
            } catch (error) {
                console.error('Error fetching diagnoses:', error);
                setError('Failed to fetch diagnoses list.');
            }
        };

        // Call the API to fetch and process patient data
        const fetchAndProcessDiagnosedPatients = async () => {
            try {
                const result = await DoctorService.getDiagnosedPatients("1"); 
                console.log('API Response - Diagnosed Patients (Raw):', result);

                const transformedData = result.map((patient, index) => {
                    const idInfo = parseEgyptianNationalId(patient.patientId);
                    return {
                        firstName: `Patient - ${index + 1}`,
                        diagnosis: patient.diagnosisName,
                        age: idInfo ? idInfo.age : 'N/A',
                        gender: idInfo ? idInfo.gender : 'N/A',
                        patientId: patient.patientId 
                    };
                });

                console.log('API Response - Diagnosed Patients (Processed):', transformedData);
                
                // Set the states with the transformed data
                setPatients(transformedData);
                setFilterPatient(transformedData);

                // Calculate initial stats
                const total = transformedData.length;
                const byDiagnosis = {};
                const byAgeRange = {};
                
                // Initialize age ranges
                ageRanges.forEach(r => {
                    byAgeRange[r.label] = 0;
                });

                // Calculate statistics
                transformedData.forEach(p => {
                    // Count by diagnosis
                    byDiagnosis[p.diagnosis] = (byDiagnosis[p.diagnosis] || 0) + 1;

                    // Count by age range
                    if (typeof p.age === 'number') {
                        const range = ageRanges.find(r => p.age >= r.min && p.age <= r.max);
                        if (range) byAgeRange[range.label]++;
                    }
                });

                setStats({ total, byDiagnosis, byAgeRange });

            } catch (error) {
                console.error('Error fetching and processing diagnosed patients:', error);
                setError('Failed to fetch and process patient data.');
            }
        };

        fetchDiagnoses();
        fetchAndProcessDiagnosedPatients();
    }, []);

    const applyFilters = () => {
        let result = [...patients];

        if (diagnosisfilter) {
            result = result.filter(p => p.diagnosis.toLowerCase().includes(diagnosisfilter.toLowerCase()));
        }

        if (agefilter) {
            const ageRange = ageRanges.find(r => r.label === agefilter);
            if (ageRange) {
                result = result.filter(p => p.age >= ageRange.min && p.age <= ageRange.max);
            }
        }

        if (genderfilter) {
            result = result.filter(p => {
                const g = p.gender.toLowerCase();
                return genderfilter.toLowerCase() === 'male'
                ? g === 'male'
                : g === 'female';
            });
            }

       
        const total = result.length;
        const byDiagnosis = {};
        result.forEach(p => {
            if (byDiagnosis[p.diagnosis]) {
            byDiagnosis[p.diagnosis]++;
            } else {
            byDiagnosis[p.diagnosis] = 1;
            }
        });

        const byAgeRange = {};
        ageRanges.forEach(r => {
        byAgeRange[r.label] = 0;
        });
        result.forEach(p => {
        const match = ageRanges.find(r => p.age >= r.min && p.age <= r.max);
        if (match) byAgeRange[match.label]++;
        });

        setStats({ total, byDiagnosis, byAgeRange });
        setFilterPatient(result);
    };
    
    const handleClearFilters = () => {
        setDiagnosisfilter('');
        setAgefilter('');
        setGenderfilter('');
        setFilterPatient(patients);
        // setStats({ total: 0, byDiagnosis: {}, byAgeRange: {} });
        const total = patients.length;
        const byDiagnosis = {};
        const byAgeRange = {};
        ageRanges.forEach(r => {
            byAgeRange[r.label] = 0;
        });

        patients.forEach(p => {
            byDiagnosis[p.diagnosis] = (byDiagnosis[p.diagnosis] || 0) + 1;

            const range = ageRanges.find(r => p.age >= r.min && p.age <= r.max);
            if (range) byAgeRange[range.label]++;
        });

        setStats({ total, byDiagnosis, byAgeRange });
  };

    return (
    <div className="research-container">
            <h1>Research Patients</h1>
            <h2>Patients available for research purposes.</h2>
            
            {successMessage && <div className="success-message">{successMessage}</div>}
            {error && <div className="error-message">{error}</div>}
            

            <div className="research-tabs">
                <button className={`research-tab ${activeTab === 'list' ? 'active' : ''}`} onClick={() => setActiveTab('list')}>List</button>
                <button className={`research-tab ${activeTab === 'stats' ? 'active' : ''}`} onClick={() => setActiveTab('stats')}>Statistics</button>
            </div>

            <form onSubmit={e => e.preventDefault()}>
                <select value={diagnosisfilter} className='research-select-diagnosis' onChange={(e) => setDiagnosisfilter(e.target.value)}>
                    <option value="" disabled hidden>Filter By Diagnosis</option>
                    {diagnoses.map((diagnosis, i) => (
                        <option key={i} value={diagnosis.diagnosisName}>{diagnosis.diagnosisName}</option>
                    ))}
                </select>
                <select value={agefilter} className= 'research-select-age' onChange={(e) => setAgefilter(e.target.value)}>
                <option value="" disabled hidden>Filter By Age</option>
                {ageRanges.map((range, i) => (
                    <option key={i} value={range.label}>{range.label}</option>
                ))}
                </select>
                <select value={genderfilter} className= 'research-select-age' onChange={(e) => setGenderfilter(e.target.value)}>
                
                <option value="" disabled hidden>Filter By Gender</option>
                <option value={'Male'}>Male</option>
                <option value={'Female'}>Female</option>
                </select>

                <button className='research-apply' onClick={applyFilters}>Apply Filters</button>
                <button className='research-clear' onClick={handleClearFilters}>Clear Filters</button>
            </form>
            {activeTab === 'list'? (
            <div className="research-list">
                {filterPatient.length === 0 ? (
                    <h2>No patients available</h2>
                ) : (
                <table>
                    <thead>
                        <tr>
                            <th>First Name</th>
                            <th>Age</th>
                            <th>Diagnosis</th>
                            <th>Gender</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filterPatient.map(patient => (
                        <tr>
                            <td>{patient.firstName}</td>
                            <td>{patient.age}</td>
                            <td>{patient.diagnosis}</td>
                            <td>{patient.gender}</td>
                        </tr>
                        ))}
                    </tbody>
                </table>
                )}
            </div>
            ) : (
            <div className="research-stats">
                {/* <h2>Total patients: {stats.total}</h2>
                {Object.keys(stats.byDiagnosis).map((diag, i) => (
                <h2 key={i}>{diag}: {stats.byDiagnosis[diag]}</h2>
                ))} */}
                <div className='stats-container'>
                <div className='research-total'>
                    <h2>{diagnosisfilter}</h2>
                    <h2>{agefilter}</h2>
                    <h2>{genderfilter}</h2>
                    <h2>Total Patients: {stats.total}</h2>                    
                </div>
                <div className='research-charts'>
                <Pie
                    data={{
                        labels: Object.keys(stats.byDiagnosis),
                        datasets: [{
                            data: Object.values(stats.byDiagnosis),
                            backgroundColor: [
                                '#FF6384',
                                '#36A2EB',
                                '#FFCE56',
                                '#4BC0C0',
                                '#9966FF',
                                '#FF9F40'
                            ],
                        }],
                    }}
                    options={{
                        responsive: true,
                        plugins: {
                            // legend: {
                            //     position: 'top',
                            // },
                            title: {
                                display: true,
                                text: 'Patients by Diagnosis'
                            }
                        }
                    }}

                />
                </div>

                <div className='research-charts'>
                <Bar
                    data={{
                        labels: Object.keys(stats.byAgeRange),
                        datasets: [{
                        label: 'Number of Patients',
                        data: Object.values(stats.byAgeRange),
                        backgroundColor: '#36A2EB'
                        }]
                    }}
                    options={{
                        responsive: true,
                        plugins: {
                        title: {
                            display: true,
                            text: 'Patients per Age Range'
                        }
                        },
                        scales: {
                        y: {
                            beginAtZero: true
                        }
                        }
                    }}
                    />
                    </div>

                    </div>

            </div>
            )}
    </div>
    );

};

export default Research;
