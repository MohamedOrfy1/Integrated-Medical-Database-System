import axios from 'axios';

const API_URL = 'http://localhost:8080';

// Configure axios defaults
axios.defaults.withCredentials = true;

export const DoctorService = {
    // Get all doctors
    getAllDoctors: async () => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                throw new Error('No authentication token found');
            }

            const response = await axios.get(`${API_URL}/doctors/getdoc`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            
            console.log('GET response status:', response.status);
            console.log('GET response data:', response.data);
            return response.data;
        } catch (error) {
            console.error('Error in getAllDoctors:', error);
            throw error;
        }
    },

    // Assign a doctor to a patient
    assignDoctorToPatient: async (PatientId, DoctorId) => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                throw new Error('No authentication token found');
            }

            const response = await axios.post(
                `${API_URL}/employee/assign`,
                {
                    PatientId,
                    DoctorId
                },
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );
            
            console.log('POST response status:', response.status);
            console.log('POST response data:', response.data);
            return response.data;
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
            
            // If response.data is false, it means the deletion failed
            if (response.data === false) {
                throw new Error('DIAGNOSIS_IN_USE');
            }
            
            return true;
        } catch (error) {
            console.error('Error deleting diagnosis:', error.response || error);
            if (error.message === 'DIAGNOSIS_IN_USE') {
                throw error;
            }
            throw new Error('DELETE_FAILED');
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

            return response.data;
        } catch (error) {
            console.error('Error fetching patient:', error);
            throw error;
        }
    },

    getDiagnosedPatients: async (sortBy) => {
        try {
            const token = localStorage.getItem('token');
            console.log('Making request to get diagnosed patients with sort:', sortBy);
            
            const response = await axios({
                method: 'post',
                url: `${API_URL}/doctors/getDiagnosedPatients`,
                data: sortBy,
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            });

            console.log('Get diagnosed patients response status:', response.status);
            console.log('Get diagnosed patients response data:', response.data);

            return response.data;
        } catch (error) {
            console.error('Error in getDiagnosedPatients:', error);
            throw error;
        }
    },

    getReportTest: async (testId) => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                throw new Error('No authentication token found');
            }

            const response = await axios.post(
                `${API_URL}/doctors/getReportTest`,
                testId,
                {
                    responseType: 'blob',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );
            
            console.log('PDF report response status:', response.status);
            return response.data;
        } catch (error) {
            console.error('Error fetching PDF report:', error);
            throw error;
        }
    },

    diagnosePatient: async (diagnosisPayload) => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.post(
                `${API_URL}/doctors/diagnosePatient`,
                JSON.stringify(diagnosisPayload),
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );
            return response.data;
        } catch (error) {
            console.error('Error diagnosing patient:', error);
            throw error;
        }
    },

    deleteTest: async (testId) => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.delete(
                `${API_URL}/doctors/deletest`,
                {
                    data: testId.toString(),
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );
            return response.data;
        } catch (error) {
            console.error('Error deleting test:', error);
            throw error;
        }
    }
}; 