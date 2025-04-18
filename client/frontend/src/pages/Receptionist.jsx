import React, { useState } from 'react';
import '../styles/Receptionist.css';

export default function Receptionist() {
    const [patientData, setPatientData] = useState({name: '',nationalId: '', phoneNumber: '', registeredDate: '' });
    const [diagnosis, setDiagnosis] = useState({name: '', code: '' });
    
    const PatientInfo = (e) => {
        const { name, value } = e.target;
        setPatientData(prev => ({
          ...prev,
          [name]: value
        }));
      };


    const DiagnosisInfo = (e) => {
    const { name, value } = e.target;
    setDiagnosis(prev => ({
        ...prev,
        [name]: value
    }));
    };

    const Register = () => {
        console.log('Registering patient:', patientData);
        alert('Patient registered successfully!');
        
        // Reset form
        setPatientData({
          name: '',
          nationalId: '',
          phoneNumber: '',
          registeredDate: '',
        });

        setDiagnosis({
            name: '',
            code: ''
        });
      };

    return (
        <div className="receptionist-container">
            {/* <h1>Receptionist Portal</h1> */}

            <div className='patient-info'>
                <h2>Patient Data</h2>
                <form className='form'>
                    <div className='info-row'>
                        <label>Name: </label>
                        <input
                        type="text"
                        name="name"
                        value={patientData.name}
                        onChange={PatientInfo}
                        placeholder="Patient Name"
                        />
                    </div>
                    <div className='info-row'>
                        <label>National ID: </label>
                        <input
                            type="text"
                            name="nationalId"
                            value={patientData.nationalId}
                            onChange={PatientInfo}
                            placeholder="National ID"
                        />
                    </div>
                    <div className='info-row'>
                        <label>Phone Number: </label>
                        <input
                            type="text"
                            name="phoneNumber"
                            value={patientData.phoneNumber}
                            onChange={PatientInfo}
                            placeholder="Phone Number"
                        />
                    </div>
                    <div className='info-row'>
                        <label>Registered Date: </label>
                        <input
                            type="date"
                            name="registeredDate"
                            value={patientData.registeredDate}
                            onChange={PatientInfo}
                        />
                    </div>
                </form>
            </div>
            <div className='diagnosis-container'>
                <div className='diagnosis-info'>
                    <h2>Diagnosis</h2>
                    <form className='form'>
                        <div className='info-row'>
                            <label>Name: </label>
                            <input
                            type="text"
                            name="name"
                            value={diagnosis.name}
                            onChange={DiagnosisInfo}
                            placeholder="Diagnosis Name"
                            />
                        </div>
                        <div className='info-row'>
                            <label>Code: </label>
                            <input
                                type="text"
                                name="code"
                                value={diagnosis.nationalId}
                                onChange={DiagnosisInfo}
                                placeholder="Code"
                            />
                        </div>
                        
                    </form>
                </div>
                <div className='buttons'>
                    <button className='register' type="button" onClick={Register}><h1>Register</h1></button>
                    <button className='register' type="button" onClick={Register}><h1>Logs</h1></button> 
                </div>  
            </div>
        </div>
    );
}
