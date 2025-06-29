import React, { useContext, useEffect, useState } from "react";
import { PatientContext } from '../Context/PatientContext';
import { DoctorService } from '../services/DoctorService';
import '../styles/Patient.css'; 

export default function PatientTests() {
    const [diagnoses, setDiagnoses] = useState([]);
    const [testIds, setTestIds] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
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
        <div className="tests-container max-w-xl mx-auto">
            <h2 className="tests-title text-2xl font-bold text-center mb-8">Patient Tests</h2>
            <div className="flex flex-col gap-10">
                <section>
                    <h3 className="text-lg font-semibold mb-3">Diagnoses</h3>
                    {diagnoses.length === 0 ? (
                        <div className="no-tests text-gray-500">No diagnoses found</div>
                    ) : (
                        diagnoses.map((diag, idx) => (
                            <div
                                key={idx}
                                className="test-card bg-white rounded-xl shadow-md p-6 mb-4"
                            >
                                <div><span className="font-semibold">Date:</span> {diag.DiagnosisDate}</div>
                                <div><span className="font-semibold">Diagnosis:</span> {diag.Diagnosis}</div>
                            </div>
                        ))
                    )}
                </section>
                <section>
                    <h3 className="text-lg font-semibold mb-3">Test IDs</h3>
                    {testIds.length === 0 ? (
                        <div className="no-tests text-gray-500">No tests found</div>
                    ) : (
                        testIds.map((testId, idx) => (
                            <div
                                key={idx}
                                className="test-card bg-white rounded-xl shadow-md p-6 mb-4"
                            >
                                <div><span className="font-semibold">Test ID:</span> {testId}</div>
                            </div>
                        ))
                    )}
                </section>
            </div>
        </div>
    );
}