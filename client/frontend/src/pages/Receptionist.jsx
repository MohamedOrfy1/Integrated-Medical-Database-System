import React, { useState, useEffect } from 'react';
import { PatientService } from '../services/PatientService';
import '../styles/Receptionist.css';

const Receptionist = () => {
    const [patients, setPatients] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [activeTab, setActiveTab] = useState('list'); // 'list' or 'form'
    const [newPatient, setNewPatient] = useState({
        patientId: '',
        firstName: '',
        fatherName: '',
        grandfatherName: '',
        familyName: '',
        phoneNumber: '',
        registrationDate: new Date().toISOString().split('T')[0],
        email: ''
    });

    useEffect(() => {
        console.log('Receptionist component mounted');
        fetchPatients();
    }, []);

    const fetchPatients = async () => {
        try {
            console.log('Fetching patients...');
            setLoading(true);
            const data = await PatientService.getAllPatients();
            console.log('Patients fetched successfully:', data);
            setPatients(data);
            setError('');
        } catch (err) {
            console.error('Error in fetchPatients:', err);
            setError('Failed to fetch patients. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        console.log(`Input changed - ${name}:`, value);
        setNewPatient(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log('Submitting new patient:', newPatient);
        try {
            setLoading(true);
            
            // Format the patient data according to the backend model
            const formattedPatient = {
                patientId: newPatient.patientId,
                firstName: newPatient.firstName,
                fatherName: newPatient.fatherName,
                grandfatherName: newPatient.grandfatherName,
                familyName: newPatient.familyName,
                phoneNumber: newPatient.phoneNumber,
                registrationDate: newPatient.registrationDate,
                email: newPatient.email,
                name: `${newPatient.firstName} ${newPatient.familyName || ''}`.trim()
            };
            
            // Convert to JSON string
            const patientJson = JSON.stringify(formattedPatient);
            console.log('Sending patient data to server:', patientJson);
            
            const response = await PatientService.addPatient(patientJson);
            console.log('Server response:', response);
            
            // Reset form
            setNewPatient({
                patientId: '',
                firstName: '',
                fatherName: '',
                grandfatherName: '',
                familyName: '',
                phoneNumber: '',
                registrationDate: new Date().toISOString().split('T')[0],
                email: ''
            });
            
            console.log('Form reset, fetching updated patient list...');
            await fetchPatients();
            setError('');
        } catch (err) {
            console.error('Error in handleSubmit:', err);
            setError(err.message || 'Failed to add patient. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="receptionist-container">
            <h1>Patient Management</h1>
            
            {error && <div className="error-message">{error}</div>}
            
            <div className="tabs">
                <button 
                    className={`tab-button ${activeTab === 'list' ? 'active' : ''}`}
                    onClick={() => setActiveTab('list')}
                >
                    Patient List
                </button>
                <button 
                    className={`tab-button ${activeTab === 'form' ? 'active' : ''}`}
                    onClick={() => setActiveTab('form')}
                >
                    Add New Patient
                </button>
            </div>

            {activeTab === 'form' ? (
                <div className="patient-form">
                    <h2>Add New Patient</h2>
                    <form onSubmit={handleSubmit}>
                        <div className="form-group">
                            <input
                                type="text"
                                name="patientId"
                                value={newPatient.patientId}
                                onChange={handleInputChange}
                                placeholder="Patient ID (14 characters)"
                                maxLength={14}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                name="firstName"
                                value={newPatient.firstName}
                                onChange={handleInputChange}
                                placeholder="First Name"
                                maxLength={20}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                name="fatherName"
                                value={newPatient.fatherName}
                                onChange={handleInputChange}
                                placeholder="Father's Name"
                                maxLength={20}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                name="grandfatherName"
                                value={newPatient.grandfatherName}
                                onChange={handleInputChange}
                                placeholder="Grandfather's Name"
                                maxLength={20}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="text"
                                name="familyName"
                                value={newPatient.familyName}
                                onChange={handleInputChange}
                                placeholder="Family Name"
                                maxLength={20}
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="tel"
                                name="phoneNumber"
                                value={newPatient.phoneNumber}
                                onChange={handleInputChange}
                                placeholder="Phone Number"
                                maxLength={11}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="date"
                                name="registrationDate"
                                value={newPatient.registrationDate}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <input
                                type="email"
                                name="email"
                                value={newPatient.email}
                                onChange={handleInputChange}
                                placeholder="Email"
                            />
                        </div>
                        <button type="submit" disabled={loading}>
                            {loading ? 'Adding...' : 'Add Patient'}
                        </button>
                    </form>
                </div>
            ) : (
                <div className="patients-list">
                    <h2>Patient List</h2>
                    {loading ? (
                        <p>Loading patients...</p>
                    ) : patients.length > 0 ? (
                        <table>
                            <thead>
                                <tr>
                                    <th>First Name</th>
                                    <th>Last Name</th>
                                    <th>Phone Number</th>
                                    <th>Registration Date</th>
                                    <th>ID</th>
                                </tr>
                            </thead>
                            <tbody>
                                {patients.map(patient => (
                                    <tr key={patient.patientId}>
                                        <td>{patient.firstName}</td>
                                        <td>{patient.familyName}</td>
                                        <td>{patient.phoneNumber}</td>
                                        <td>{patient.registrationDate}</td>
                                        <td>{patient.patientId}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    ) : (
                        <p>No patients found</p>
                    )}
                </div>
            )}
        </div>
    );
};

export default Receptionist;
