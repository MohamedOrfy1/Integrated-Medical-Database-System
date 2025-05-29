import axios from 'axios';

const API_URL = 'https://religious-tammie-tamim21-353bd377.koyeb.app';

// Configure axios defaults
axios.defaults.withCredentials = true;

export const DoctorService = {
    // Get all doctors
    getAllDoctors: async () => {
        try {
            console.log('Making GET request to:', `${API_URL}/doctors/getdoc`);
            const response = await fetch(`${API_URL}/doctors/getdoc`);
            console.log('GET response status:', response.status);
            
            if (!response.ok) {
                console.error('GET request failed with status:', response.status);
                throw new Error('Failed to fetch doctors');
            }
            
            const data = await response.json();
            console.log('GET response data:', data);
            return data;
        } catch (error) {
            console.error('Error in getAllDoctors:', error);
            throw error;
        }
    },

    // Assign a doctor to a patient
    assignDoctorToPatient: async (PatientId, DoctorId) => {
        try {
            console.log('Making POST request to:', `${API_URL}/employee/assign`);
            const requestBody = JSON.stringify({
                PatientId,
                DoctorId
            });
            console.log('Request body:', requestBody);
            
            const response = await fetch(`${API_URL}/employee/assign`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: requestBody,
            });
            
            console.log('POST response status:', response.status);
            
            if (!response.ok) {
                const errorData = await response.text();
                console.error('POST request failed with status:', response.status);
                console.error('Error response:', errorData);
                throw new Error(`Failed to assign doctor: ${errorData}`);
            }
            
            const data = await response.json();
            console.log('POST response data:', data);
            return data;
        } catch (error) {
            console.error('Error in assignDoctorToPatient:', error);
            throw error;
        }
    },

    getAssignedPatients: async () => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                throw new Error('No authentication token found');
            }

            const response = await axios.post(`${API_URL}/doctors/getDocPatients`, null, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            console.error('Error fetching assigned patients:', error);
            throw error;
        }
    },

    getDiagnosisList: async () => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.get(`${API_URL}/doctors/getDiagnosis`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const data = typeof response.data === 'string' ? JSON.parse(response.data) : response.data;
            return data;
        } catch (error) {
            console.error('Error fetching diagnosis list:', error);
            throw error;
        }
    },

    addDiagnosis: async (diagnosisData) => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.post(
                `${API_URL}/doctors/diagnosePatient`,
                JSON.stringify(diagnosisData),
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );
            return response.data;
        } catch (error) {
            console.error('Error adding diagnosis:', error);
            throw error;
        }
    }
}; 