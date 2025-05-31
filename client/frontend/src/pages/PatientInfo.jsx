import React, { useContext, useEffect, useState } from "react";
import { PatientContext } from '../Context/PatientContext';
import { useNavigate } from 'react-router-dom';
import { DoctorService } from '../services/DoctorService';

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
            <div className="flex justify-center items-center min-h-screen">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-400"></div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative max-w-xl mx-auto mt-4" role="alert">
                <strong className="font-bold">Error! </strong>
                <span className="block sm:inline">{error}</span>
            </div>
        );
    }

    if (!patient) {
        return (
            <div className="bg-yellow-100 border border-yellow-400 text-yellow-700 px-4 py-3 rounded relative max-w-xl mx-auto mt-4" role="alert">
                <strong className="font-bold">No Data Available</strong>
                <span className="block sm:inline"> Patient information not found.</span>
            </div>
        );
    }

    return (
        <div className="bg-white rounded-2xl p-6 max-w-xl mx-auto shadow text-gray-900 space-y-6">
            <div className="text-xl font-bold text-blue-400">
                {patient.firstName} {patient.familyName}
            </div>

            <div className="grid grid-cols-2 gap-y-4 gap-x-6 text-sm">
                <div>
                    <p className="text-gray-500">Registration Date</p>
                    <p>{new Date(patient.registrationDate).toLocaleDateString()}</p>
                </div>
                <div>
                    <p className="text-gray-500">Age</p>
                    <p>{patient.age}</p>
                </div>
                <div>
                    <p className="text-gray-500">Gender</p>
                    <p>{patient.gender}</p>
                </div>
                <div>
                    <p className="text-gray-500">Phone Number</p>
                    <p>{patient.phoneNumber}</p>
                </div>
                <div>
                    <p className="text-gray-500">Address</p>
                    <p>{patient.address}</p>
                </div>
                <div>
                    <p className="text-gray-500">Blood Type</p>
                    <p>{patient.bloodType}</p>
                </div>
            </div>
        </div>
    );
}





