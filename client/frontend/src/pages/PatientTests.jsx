import React, { useContext, useEffect, useState } from "react";
import { PatientContext } from '../Context/PatientContext';
import { DoctorService } from '../services/DoctorService';
import '../styles/Patient.css'; 

export default function PatientTests() {
    const [tests, setTests] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { patientId } = useContext(PatientContext);

    const fetchTests = async () => {
        try {
            setLoading(true);
            const patientData = await DoctorService.getPatient(patientId);
            console.log('Received patient data:', patientData);
            
            // Extract tests from patient data
            if (patientData && patientData.tests) {
                setTests(patientData.tests);
            } else {
                setTests([]);
            }
        } catch (error) {
            console.error('Error fetching tests:', error);
            setError(error.message);
        } finally {
            setLoading(false);
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
        <div className="tests-container">
            <h2 className="tests-title">Patient Tests</h2>
            <div className="tests-list">
                {tests.length === 0 ? (
                    <div className="no-tests">No tests found</div>
                ) : (
                    tests.map((test) => (
                        <div key={test.id} className="test-card">
                            <div className="test-header">
                                <div>
                                    <div className="test-name">
                                        {test.testName || test.name}
                                    </div>
                                    <div className="test-type">
                                        {test.testType || test.type}
                                    </div>
                                </div>
                                <div className="test-date-container">
                                    <div>{test.date || test.testDate}</div>
                                    <div className="test-status">
                                        {test.status || 'Completed'}
                                    </div>
                                </div>
                            </div>

                            <div className="test-actions">
                                <button className="test-view-btn">
                                    View Results
                                </button>
                                <button className="test-download-btn">
                                    Download
                                </button>
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}