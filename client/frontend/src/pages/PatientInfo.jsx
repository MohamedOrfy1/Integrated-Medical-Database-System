import React, { useContext, useEffect, useState } from "react";
import axios from "axios";
import { PatientContext } from '../Context/PatientContext';

export default function PatientInfo() {
    const [patient, setPatient] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { patientId } = useContext(PatientContext);

    const getPatientInfo = async () => {
        try {
            setLoading(true);
            setError(null);
            const token = localStorage.getItem('token');
            const response = await axios.post(
                'https://religious-tammie-tamim21-353bd377.koyeb.app/getPatient',
                JSON.stringify({ PatientID: patientId }),
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );

            if (response.data) {
                const patientData = JSON.parse(response.data);
                setPatient(patientData);
            }
        } catch (err) {
            console.error('Error fetching patient info:', err);
            setError('Failed to fetch patient information');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (patientId) {
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





