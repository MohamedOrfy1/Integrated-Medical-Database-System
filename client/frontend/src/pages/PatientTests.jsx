import React, { useContext, useEffect, useState } from "react";
import { PatientContext } from '../Context/PatientContext';
import { DoctorService } from '../services/DoctorService';
import '../styles/Patient.css'; 

export default function PatientTests() {
    const [diagnoses, setDiagnoses] = useState([]);
    const [testIds, setTestIds] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [downloading, setDownloading] = useState(false);
    const { patientId } = useContext(PatientContext);

    const fetchTests = async () => {
        try {
            setLoading(true);
            const patientData = await DoctorService.getPatient(patientId);
            console.log('Received patient data:', patientData);

            setDiagnoses(patientData.Diagnosis || []);
            setTestIds(patientData.TestIDs || []);
        } catch (error) {
            console.error('Error fetching tests:', error);
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    const downloadPDFReport = async (testId) => {
        try {
            setDownloading(true);
            console.log('Downloading PDF for test ID:', testId);
            
            const response = await DoctorService.getReportTest(testId);
            
            // Create a download link for the PDF
            const url = window.URL.createObjectURL(response);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `test_report_${testId}.pdf`);
            document.body.appendChild(link);
            link.click();
            link.remove();
            
            console.log('PDF download completed for test ID:', testId);
        } catch (error) {
            console.error('Error downloading PDF:', error);
            setError('Failed to download PDF report. Please try again.');
        } finally {
            setDownloading(false);
        }
    };

    useEffect(() => {
        if (patientId) {
            fetchTests();
        }
    }, [patientId]);

    if (loading) {
        return (
            <div className="loading-container">
                <div className="loading-spinner"></div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="alert alert-error" role="alert">
                <strong className="alert-title">Error! </strong>
                <span className="alert-message">{error}</span>
            </div>
        );
    }

    return (
        <div className="tests-container modern-card">
            <h2 className="tests-title">Patient Tests & Diagnoses</h2>
            <div className="diagnosis-section">
                <h3 className="section-subtitle">Diagnoses</h3>
                {diagnoses.length === 0 ? (
                    <div className="no-tests">No diagnoses found</div>
                ) : (
                    <div className="diagnosis-list">
                        {diagnoses.map((diag, idx) => (
                            <div key={idx} className="diagnosis-card">
                                <div><span className="diagnosis-label">Date:</span> {diag.DiagnosisDate}</div>
                                <div><span className="diagnosis-label">Diagnosis:</span> {diag.Diagnosis}</div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
            <div className="test-section">
                <h3 className="section-subtitle">Test IDs</h3>
                {testIds.length === 0 ? (
                    <div className="no-tests">No tests found</div>
                ) : (
                    <div className="test-list">
                        {testIds.map((testId, idx) => (
                            <div key={idx} className="test-card">
                                <div className="test-id-row">
                                    <span className="test-id-label">Test ID:</span>
                                    <button
                                        onClick={() => downloadPDFReport(testId)}
                                        disabled={downloading}
                                        className="test-download-btn"
                                    >
                                        {downloading ? 'Downloading...' : testId}
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}