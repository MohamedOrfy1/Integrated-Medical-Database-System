import React, { useState, useEffect } from 'react';
import { PatientService } from '../services/PatientService';
import { DoctorService } from '../services/DoctorService';
import '../styles/Receptionist.css';

const Receptionist = () => {
    const [patients, setPatients] = useState([]);
    const [loading, setLoading] = useState(false);
    const [modalLoading, setModalLoading] = useState(false);
    const [error, setError] = useState('');
    const [activeTab, setActiveTab] = useState('list');
    const [showAssignModal, setShowAssignModal] = useState(false);
    const [selectedPatient, setSelectedPatient] = useState(null);
    const [doctors, setDoctors] = useState([]);
    const [selectedDoctor, setSelectedDoctor] = useState('');
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
    const [assignedPatients, setAssignedPatients] = useState([]);
    const [patientId, setPatientId] = useState('');
    const [visitDate, setVisitDate] = useState('');
    const [visitResult, setVisitResult] = useState(null);
    const [filteredPatients, setFilteredPatients] = useState([]);
    const [filterDate, setFilterDate] = useState('');

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

    const handleAssignClick = async (patient) => {
        try {
            setSelectedPatient(patient);
            setModalLoading(true);
            const doctorsData = await DoctorService.getAllDoctors();
            setDoctors(doctorsData);
            setShowAssignModal(true);
        } catch (err) {
            console.error('Error fetching doctors:', err);
            setError('Failed to fetch doctors. Please try again.');
        } finally {
            setModalLoading(false);
        }
    };

    const handleAssignSubmit = async () => {
        try {
            setModalLoading(true);
            const assignResult = await DoctorService.assignDoctorToPatient(selectedPatient.patientId, selectedDoctor);
            if (assignResult === true) {
                setAssignedPatients(prev => [...prev, selectedPatient.patientId]);
                setShowAssignModal(false);
                setSelectedPatient(null);
                setSelectedDoctor('');
                setError('');
                await fetchPatients();
            } else {
                setError('Assignment failed. Please try again.');
            }
        } catch (err) {
            console.error('Error in handleAssignSubmit:', err);
            setError('Failed to assign patient to doctor. Please try again.');
        } finally {
            setModalLoading(false);
        }
    };

    const formatDate = (dateString) => {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    };

    const handleFilterByDate = (e) => {
        e.preventDefault();
        console.log('Filter by Date button pressed. Date:', filterDate);
        if (!filterDate) {
            setFilteredPatients([]);
            setError('');
            return;
        }
        const filtered = patients.filter(p => {
            if (!p.registrationDate) return false;
            let regDate = '';
            if (Array.isArray(p.registrationDate) && p.registrationDate.length === 3) {
                const [year, month, day] = p.registrationDate;
                regDate = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
            } else if (typeof p.registrationDate === 'string') {
                if (p.registrationDate.includes(',')) {
                    const monthMap = {
                        Jan: '01', Feb: '02', Mar: '03', Apr: '04', May: '05', Jun: '06',
                        Jul: '07', Aug: '08', Sep: '09', Oct: '10', Nov: '11', Dec: '12'
                    };
                    const [mon, day, year] = p.registrationDate.replace(',', '').split(' ');
                    regDate = `${year}-${monthMap[mon]}-${day.padStart(2, '0')}`;
                } else if (/^\d{4}-\d{2}-\d{2}/.test(p.registrationDate)) {
                    regDate = p.registrationDate.slice(0, 10);
                }
            } else if (p.registrationDate instanceof Date) {
                regDate = p.registrationDate.toISOString().split('T')[0];
            }
            console.log('Patient:', p, 'registrationDate:', p.registrationDate, 'parsed regDate:', regDate, 'filterDate:', filterDate);
            return regDate === filterDate;
        });
        console.log('Filtered patients:', filtered);
        setFilteredPatients(filtered);
        setError('');
    };
    
    const handleClearFilter = () => {
        setFilterDate('');
        setFilteredPatients([]);
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
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <h2>Patient List</h2>
                        <form onSubmit={handleFilterByDate} style={{ display: 'flex', alignItems: 'center', gap: '0', background: '#f5f8fa', padding: '8px 16px', borderRadius: '8px', boxShadow: '0 1px 4px rgba(0,0,0,0.04)' }}>
                            <input
                                type="date"
                                value={filterDate}
                                onChange={(e) => setFilterDate(e.target.value)}
                                style={{
                                    height: '38px',
                                    border: '1px solid #ccc',
                                    borderRadius: '8px 0 0 8px',
                                    padding: '0 12px',
                                    fontSize: '1rem',
                                    outline: 'none',
                                    background: '#fff',
                                }}
                                placeholder="Select date"
                            />
                            <button
                                type="submit"
                                style={{
                                    height: '38px',
                                    background: '#2196f3',
                                    color: '#fff',
                                    border: 'none',
                                    borderRadius: '0',
                                    padding: '0 18px',
                                    fontWeight: 'bold',
                                    fontSize: '1rem',
                                    cursor: 'pointer',
                                    transition: 'background 0.2s',
                                }}
                            >
                                Filter by Date
                            </button>
                            <button
                                type="button"
                                onClick={handleClearFilter}
                                style={{
                                    height: '38px',
                                    background: '#fff',
                                    color: '#2196f3',
                                    border: '1px solid #2196f3',
                                    borderRadius: '0 8px 8px 0',
                                    padding: '0 14px',
                                    fontWeight: 'bold',
                                    fontSize: '1rem',
                                    cursor: 'pointer',
                                    marginLeft: '-1px',
                                    transition: 'background 0.2s, color 0.2s',
                                }}
                            >
                                Clear Filter
                            </button>
                        </form>
                    </div>
                    {loading ? (
                        <p>Loading patients...</p>
                    ) : (filteredPatients.length > 0 ? (
                        <table>
                            <thead>
                                <tr>
                                    <th>First Name</th>
                                    <th>Last Name</th>
                                    <th>Phone Number</th>
                                    <th>Registration Date</th>
                                    <th>ID</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {filteredPatients.map(patient => (
                                    <tr key={patient.patientId}>
                                        <td>{patient.firstName}</td>
                                        <td>{patient.familyName}</td>
                                        <td>{patient.phoneNumber}</td>
                                        <td>{formatDate(patient.registrationDate)}</td>
                                        <td>{patient.patientId}</td>
                                        <td>
                                            <button 
                                                className="assign-button"
                                                onClick={() => handleAssignClick(patient)}
                                                disabled={assignedPatients.includes(patient.patientId)}
                                            >
                                                {assignedPatients.includes(patient.patientId) ? 'Assigned' : 'Assign to Doctor'}
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    ) : (
                        <table>
                            <thead>
                                <tr>
                                    <th>First Name</th>
                                    <th>Last Name</th>
                                    <th>Phone Number</th>
                                    <th>Registration Date</th>
                                    <th>ID</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {patients.map(patient => (
                                    <tr key={patient.patientId}>
                                        <td>{patient.firstName}</td>
                                        <td>{patient.familyName}</td>
                                        <td>{patient.phoneNumber}</td>
                                        <td>{formatDate(patient.registrationDate)}</td>
                                        <td>{patient.patientId}</td>
                                        <td>
                                            <button 
                                                className="assign-button"
                                                onClick={() => handleAssignClick(patient)}
                                                disabled={assignedPatients.includes(patient.patientId)}
                                            >
                                                {assignedPatients.includes(patient.patientId) ? 'Assigned' : 'Assign to Doctor'}
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    ))}
                </div>
            )}

            {showAssignModal && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>Assign Doctor</h2>
                        <p>Assigning doctor for patient: {selectedPatient?.name}</p>
                        <div className="form-group">
                            <select
                                value={selectedDoctor}
                                onChange={(e) => setSelectedDoctor(e.target.value)}
                                required
                                disabled={modalLoading}
                            >
                                <option value="">Select a doctor</option>
                                {doctors.map((doctor) => (
                                    <option key={doctor.doctorId} value={doctor.doctorId}>
                                        {doctor.firstName} {doctor.lastName} ({doctor.docType})
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div className="modal-buttons">
                            <button onClick={handleAssignSubmit} disabled={modalLoading || !selectedDoctor}>
                                {modalLoading ? 'Assigning...' : 'Assign'}
                            </button>
                            <button onClick={() => {
                                setShowAssignModal(false);
                                setSelectedPatient(null);
                                setSelectedDoctor('');
                            }} disabled={modalLoading}>
                                Cancel
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Receptionist;
