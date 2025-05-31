import React, { useContext, useEffect, useState } from "react";
import { PatientContext } from '../Context/PatientContext';
import { DoctorService } from '../services/DoctorService';

export default function PatientVisits() {
    const [visits, setVisits] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { patientId } = useContext(PatientContext);

    const fetchVisits = async () => {
        try {
            setLoading(true);
            const patientData = await DoctorService.getPatient(patientId);
            console.log('Received patient data:', patientData);
            
            // Extract visits from patient data
            if (patientData && patientData.visits) {
                setVisits(patientData.visits);
            } else {
                setVisits([]);
            }
        } catch (error) {
            console.error('Error fetching visits:', error);
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (patientId) {
            fetchVisits();
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

    return (
        <div className="max-w-3xl mx-auto p-4 bg-gray-50 min-h-screen">
            <h2 className="text-2xl font-semibold mb-6 text-blue-400">Last Visits</h2>
            <div className="space-y-6">
                {visits.length === 0 ? (
                    <div className="text-center text-gray-500">No visits found</div>
                ) : (
                    visits.map((visit) => (
                        <div
                            key={visit.id}
                            className="bg-white rounded-xl shadow-md border border-gray-200 p-5"
                        >
                            <div className="flex justify-between items-start mb-4">
                                <div>
                                    <div className="text-lg font-semibold text-blue-600">
                                        {visit.doctor}
                                    </div>
                                </div>
                                <div className="text-right text-sm text-gray-600">
                                    <div>{visit.date}</div>
                                </div>
                            </div>

                            <div className="flex gap-3">
                                <button className="bg-blue-100 hover:bg-blue-200 text-sm px-4 py-2 rounded-md">
                                    View Test
                                </button>
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
}