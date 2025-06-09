import React, { useContext, useEffect, useState } from "react";
import { PatientContext } from '../Context/PatientContext';
import { useNavigate } from 'react-router-dom';
import { DoctorService } from '../services/DoctorService';
import '../styles/Patient.css'; 

export default function PatientInfo() {
    const [patient, setPatient] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { patientId } = useContext(PatientContext);
    const navigate = useNavigate();

    const getPatientInfo = async () => {
        try {
            setLoading(true);
            setError(null);
            const token = localStorage.getItem('token');
            const role = localStorage.getItem('role');
            
            console.log('Current token:', token);
            console.log('Current role:', role);
            console.log('Current patientId:', patientId);

            if (!token) {
                console.log('No token found, redirecting to login');
                navigate('/login');
                return;
            }

            if (!patientId) {
                console.log('No patientId found');
                setError('No patient ID provided');
                return;
            }

            const patientData = await DoctorService.getPatient(patientId);
            console.log('Received patient data:', patientData);
            setPatient(patientData);

        } catch (err) {
            console.error('Error details:', {
                status: err.response?.status,
                data: err.response?.data,
                headers: err.response?.headers,
                message: err.message
            });
            
            if (err.response?.status === 403) {
                console.log('Caught 403 error, clearing auth data');
                localStorage.removeItem('token');
                localStorage.removeItem('role');
                navigate('/login');
            } else {
                setError(err.message || 'Failed to fetch patient information');
            }
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (patientId) {
            console.log('PatientId changed:', patientId);
            getPatientInfo();
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

    if (!patient) {
        return (
            <div className="alert alert-warning" role="alert">
                <strong className="alert-title">No Data Available</strong>
                <span className="alert-message"> Patient information not found.</span>
            </div>
        );
    }

    return (
        <div className="patient-info-card">
            <div className="patient-name">
                {patient.first_name} {patient.family_name}
            </div>
            <div className="patient-details-grid">
                <div className="patient-field">
                    <p className="patient-field-label">Registration Date</p>
                    <p className="patient-field-value">
                        {new Date(patient.registrationDate).toLocaleDateString()}
                    </p>
                </div>
                <div className="patient-field">
                    <p className="patient-field-label">Age</p>
                    <p className="patient-field-value">{patient.age}</p>
                </div>
            </div>
        </div>
    );
}

