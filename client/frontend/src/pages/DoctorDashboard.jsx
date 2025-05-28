import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { DoctorService } from '../services/DoctorService';
import axios from 'axios';
import '../styles/DoctorDashboard.css';

const DoctorDashboard = () => {
    const [patients, setPatients] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [showDiagnosisModal, setShowDiagnosisModal] = useState(false);
    const [selectedPatient, setSelectedPatient] = useState(null);
    const [diagnosisData, setDiagnosisData] = useState({
        diagnosisId: '',
        diagnosisDate: '',
    });
    const [diagnosisList, setDiagnosisList] = useState([]);
    const [successMessage, setSuccessMessage] = useState('');
    const [filterDiagnosis, setFilterDiagnosis] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
            navigate('/login');
            return;
        }
        fetchPatients();
        fetchDiagnosisList();
    }, [navigate]);

    const fetchPatients = async () => {
        try {
            setLoading(true);
            setError('');
            const data = await DoctorService.getAssignedPatients();
            setPatients(data);
        } catch (err) {
            console.error('Error fetching patients:', err);
            if (err.response?.status === 403) {
                localStorage.removeItem('token');
                localStorage.removeItem('role');
                navigate('/login');
            } else {
                setError('Failed to fetch patients. Please try again.');
            }
        } finally {
            setLoading(false);
        }
    };

    const fetchDiagnosisList = async () => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.get('http://localhost:8080/doctors/getDiagnosis', {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const data = typeof response.data === 'string' ? JSON.parse(response.data) : response.data;
            setDiagnosisList(data);
        } catch (err) {
            setDiagnosisList([]);
        }
    };

    const handleAddDiagnosis = (patient) => {
        setSelectedPatient(patient);
        setDiagnosisData({
            diagnosisId: '',
            diagnosisDate: '',
        });
        setShowDiagnosisModal(true);
    };

    const handleDiagnosisSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem('token');
            const payload = {
                DiagnosisId: diagnosisData.diagnosisId,
                DiagnosisDate: diagnosisData.diagnosisDate,
                PatientId: selectedPatient.patientId
            };
            const response = await axios.post(
                'http://localhost:8080/doctors/diagnosePatient',
                JSON.stringify(payload),
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );
            if (response.data === true) {
                setSuccessMessage('Diagnosis added successfully!');
                setShowDiagnosisModal(false);
                const selectedDiagnosis = diagnosisList.find(
                  d => d.diagnosisCode === diagnosisData.diagnosisId
                );
                setPatients(prevPatients =>
                  prevPatients.map(p =>
                    p.patientId === selectedPatient.patientId
                      ? {
                          ...p,
                          latestDiagnosis: {
                            diagnosisId: diagnosisData.diagnosisId,
                            diagnosisName: selectedDiagnosis ? selectedDiagnosis.diagnosisName : '',
                            diagnosisDate: diagnosisData.diagnosisDate
                          }
                        }
                      : p
                  )
                );
            } else {
                setError('Failed to add diagnosis. Please try again.');
            }
        } catch (err) {
            setError('Failed to add diagnosis. Please try again.');
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setDiagnosisData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleFilterByDiagnosis = async (diagnosisId) => {
        try {
            const token = localStorage.getItem('token');
            const payload = JSON.stringify({ DiagnosisId: diagnosisId });
            const response = await axios.post(
                'http://localhost:8080/doctors/getPatDiagnosis',
                payload,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );
            const data = typeof response.data === 'string' ? JSON.parse(response.data) : response.data;
            setPatients(data);
        } catch (err) {
            setError('Failed to filter patients by diagnosis.');
        }
    };

    if (loading) {
        return <div className="loading">Loading patients...</div>;
    }

    if (error) {
        return <div className="error">{error}</div>;
    }

    return (
        <div className="doctor-dashboard">
            <h1>Doctor Dashboard</h1>
            {successMessage && (
                <div className="success-message">
                    {successMessage}
                    <button onClick={() => setSuccessMessage('')}>×</button>
                </div>
            )}
            <div className="filter-bar" style={{ marginBottom: '1.5rem', display: 'flex', gap: '1rem', alignItems: 'center' }}>
                <select
                    value={filterDiagnosis}
                    onChange={async (e) => {
                        const code = e.target.value;
                        setFilterDiagnosis(code);
                        if (code) {
                            await handleFilterByDiagnosis(code);
                        } else {
                            fetchPatients();
                        }
                    }}
                    style={{ padding: '0.5rem', borderRadius: '8px', border: '1.5px solid #e0e0e0', fontSize: '1rem' }}
                >
                    <option value="">All Diagnoses</option>
                    {diagnosisList.map((diag) => (
                        <option key={diag.diagnosisCode} value={diag.diagnosisCode}>
                            {diag.diagnosisName}
                        </option>
                    ))}
                </select>
                {filterDiagnosis && (
                    <button
                        onClick={() => { setFilterDiagnosis(''); fetchPatients(); }}
                        style={{ padding: '0.5rem 1rem', borderRadius: '8px', border: 'none', background: '#f44336', color: '#fff', fontWeight: 'bold', cursor: 'pointer' }}
                    >
                        Clear Filter
                    </button>
                )}
            </div>
            <div className="patients-list">
                <h2>Your Patients</h2>
                {patients.length === 0 ? (
                    <p>No patients assigned yet.</p>
                ) : (
                    <table>
                        <thead>
                            <tr>
                                <th>Patient ID</th>
                                <th>Name</th>
                                <th>Registration Date</th>
                                <th>Diagnosis</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {patients.map((patient) => (
                                <tr key={patient.patientId}>
                                    <td>{patient.patientId ? `****${patient.patientId.slice(-4)}` : ''}</td>
                                    <td>{[patient.firstName, patient.fatherName, patient.grandfatherName, patient.familyName].filter(Boolean).join(' ')}</td>
                                    <td>{new Date(patient.registrationDate).toLocaleDateString()}</td>
                                    <td>
                                        {patient.latestDiagnosis
                                            ? `${
                                                patient.latestDiagnosis.diagnosisName ||
                                                diagnosisList.find(d => d.diagnosisCode === patient.latestDiagnosis.diagnosisId)?.diagnosisName ||
                                                patient.latestDiagnosis.diagnosisId
                                            } (${patient.latestDiagnosis.diagnosisDate})`
                                            : '—'}
                                    </td>
                                    <td>
                                        <button onClick={() => handleAddDiagnosis(patient)}>
                                            Add Diagnosis
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                )}
            </div>
            {showDiagnosisModal && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>Add Diagnosis for Patient {selectedPatient && selectedPatient.patientId.slice(-4)}</h2>
                        <form onSubmit={handleDiagnosisSubmit}>
                            <div className="form-group">
                                <select
                                    id="diagnosisId"
                                    name="diagnosisId"
                                    value={diagnosisData.diagnosisId}
                                    onChange={handleInputChange}
                                    required
                                    className={diagnosisData.diagnosisId ? 'has-value' : ''}
                                >
                                    <option value="" disabled>Select Diagnosis</option>
                                    {diagnosisList.map((diag) => (
                                        <option key={diag.diagnosisCode} value={diag.diagnosisCode}>
                                            {diag.diagnosisName}
                                        </option>
                                    ))}
                                </select>
                                <label htmlFor="diagnosisId">Diagnosis Name</label>
                            </div>
                            <div className="form-group">
                                <input
                                    id="diagnosisDate"
                                    name="diagnosisDate"
                                    type="date"
                                    value={diagnosisData.diagnosisDate}
                                    onChange={handleInputChange}
                                    required
                                    placeholder=" "
                                />
                                <label htmlFor="diagnosisDate">Diagnosis Date</label>
                            </div>
                            <div className="modal-buttons">
                                <button type="submit" className="submit-btn">Submit</button>
                                <button 
                                    type="button" 
                                    className="cancel-btn"
                                    onClick={() => setShowDiagnosisModal(false)}
                                >
                                    Cancel
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default DoctorDashboard; 