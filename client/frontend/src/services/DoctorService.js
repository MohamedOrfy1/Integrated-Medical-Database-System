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
            const response = await axios.get(
                `${API_URL}/doctors/getDiagnosis`,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );
            return response.data;
        } catch (error) {
            console.error('Error fetching diagnosis list:', error);
            throw error;
        }
    },

    addDiagnosis: async (diagnosisData) => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.post(
                `${API_URL}/doctors/addDiagnosis`,
                {
                    diagnosisCode: diagnosisData.diagnosisCode,
                    diagnosisName: diagnosisData.diagnosisName
                },
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
    },

    // New methods for managing diagnoses
    createDiagnosis: async (diagnosisData) => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.post(
                `${API_URL}/doctors/createDiagnosis`,
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
            console.error('Error creating diagnosis:', error);
            throw error;
        }
    },

    // Delete a diagnosis
    deleteDiagnosis: async (diagnosisCode) => {
        try {
            const token = localStorage.getItem('token');
            console.log('Attempting to delete diagnosis:', diagnosisCode);
            
            const response = await axios.delete(
                `${API_URL}/doctors/diagnoses/${diagnosisCode}`,
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );
            
            console.log('Delete response:', response);
            
            // The backend returns a boolean in the response body
            return response.data === true;
        } catch (error) {
            console.error('Error deleting diagnosis:', error.response || error);
            // If the error is due to the diagnosis being in use, return false
            if (error.response?.status === 400) {
                return false;
            }
            throw error;
        }
    },

    getPatient: async (patientId) => {
        try {
            const token = localStorage.getItem('token');
            console.log('Making request to get patient info:', patientId);
            
            const response = await axios({
                method: 'post',
                url: `${API_URL}/doctors/getPatient`,
                data: { PatientID: patientId },
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            });

            console.log('Response status:', response.status);
            console.log('Response data:', response.data);

            if (response.status === 403) {
                throw new Error('Access forbidden');
            }

            if (!response.data) {
                throw new Error('No data received');
            }

            return response.data;
        } catch (error) {
            console.error('Error in getPatient:', error);
            throw error;
        }
    }
}; 