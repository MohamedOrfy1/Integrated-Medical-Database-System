import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { DoctorService } from '../services/DoctorService';
import '../styles/DoctorDashboard.css';
import { PatientContext } from '../Context/PatientContext';


const DoctorDashboard = () => {
    let{patientId,setPatientId}=useContext(PatientContext);
    const [patients, setPatients] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [showDiagnosisModal, setShowDiagnosisModal] = useState(false);
    const [showManageDiagnosisModal, setShowManageDiagnosisModal] = useState(false);
    const [selectedPatient, setSelectedPatient] = useState(null);
    const [diagnosisData, setDiagnosisData] = useState({
        diagnosisId: '',
        diagnosisDate: '',
    });
    const [newDiagnosis, setNewDiagnosis] = useState({
        diagnosisCode: '',
        diagnosisName: '',
    });
    const [diagnosisList, setDiagnosisList] = useState([]);
    const [successMessage, setSuccessMessage] = useState('');
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
            const data = await DoctorService.getDiagnosisList();
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
            const payload = {
                DiagnosisId: diagnosisData.diagnosisId,
                DiagnosisDate: diagnosisData.diagnosisDate,
                PatientId: selectedPatient.patientId
            };
            
            const success = await DoctorService.addDiagnosis(payload);
            
            if (success) {
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

    const handleCreateDiagnosis = async (e) => {
        e.preventDefault();
        try {
            const response = await DoctorService.addDiagnosis(newDiagnosis);
            if (response === "Diagnosis added successfully") {
                setSuccessMessage('Diagnosis created successfully!');
                setNewDiagnosis({ diagnosisCode: '', diagnosisName: '' });
                fetchDiagnosisList();
            } else {
                setError(response || 'Failed to create diagnosis. Please try again.');
            }
        } catch (err) {
            setError('Failed to create diagnosis. Please try again.');
        }
    };

    const handleDeleteDiagnosis = async (diagnosisCode) => {
        try {
            const success = await DoctorService.deleteDiagnosis(diagnosisCode);
            if (success) {
                setSuccessMessage('Diagnosis deleted successfully!');
                fetchDiagnosisList();
            } else {
                setError('Cannot delete diagnosis: It may be in use by existing patient records.');
            }
        } catch (err) {
            console.error('Delete diagnosis error:', err);
            setError('Failed to delete diagnosis. Please try again later.');
        }
    };

    const handleNewDiagnosisChange = (e) => {
        const { name, value } = e.target;
        setNewDiagnosis(prev => ({
            ...prev,
            [name]: value
        }));
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
            <div className="dashboard-actions">
                <button 
                    className="manage-diagnosis-btn"
                    onClick={() => setShowManageDiagnosisModal(true)}
                >
                    Manage Diagnoses
                </button>
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
                                    <button
                                        style={{ marginLeft: "8px" }}
                                        onClick={() =>{ setPatientId(patient.patientId);
                                            console.log(patientId)
                                            navigate(`/patient`);
                                            }}
                                        > View Details
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

            {showManageDiagnosisModal && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>Manage Diagnoses</h2>
                        
                        <form onSubmit={handleCreateDiagnosis} className="create-diagnosis-form">
                            <h3>Create New Diagnosis</h3>
                            <div className="form-group">
                                <input
                                    type="text"
                                    name="diagnosisCode"
                                    value={newDiagnosis.diagnosisCode}
                                    onChange={handleNewDiagnosisChange}
                                    placeholder="Diagnosis Code"
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <input
                                    type="text"
                                    name="diagnosisName"
                                    value={newDiagnosis.diagnosisName}
                                    onChange={handleNewDiagnosisChange}
                                    placeholder="Diagnosis Name"
                                    required
                                />
                            </div>
                            <button type="submit" className="submit-btn">Create Diagnosis</button>
                        </form>

                        <div className="diagnosis-list">
                            <h3>Existing Diagnoses</h3>
                            <table>
                                <thead>
                                    <tr>
                                        <th>Code</th>
                                        <th>Name</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {diagnosisList.map((diagnosis) => (
                                        <tr key={diagnosis.diagnosisCode}>
                                            <td>{diagnosis.diagnosisCode}</td>
                                            <td>{diagnosis.diagnosisName}</td>
                                            <td>
                                                <button
                                                    className="delete-btn"
                                                    onClick={() => handleDeleteDiagnosis(diagnosis.diagnosisCode)}
                                                >
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>

                        <div className="modal-buttons">
                            <button
                                className="cancel-btn"
                                onClick={() => setShowManageDiagnosisModal(false)}
                            >
                                Close
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default DoctorDashboard; 