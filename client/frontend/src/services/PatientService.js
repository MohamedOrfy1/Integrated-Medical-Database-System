const API_URL = 'http://localhost:8080/patients';

export const PatientService = {
    // Get all patients
    getAllPatients: async () => {
        try {
            console.log('Making GET request to:', `${API_URL}/getPatients`);
            const response = await fetch(`${API_URL}/getPatients`);
            console.log('GET response status:', response.status);
            
            if (!response.ok) {
                console.error('GET request failed with status:', response.status);
                throw new Error('Failed to fetch patients');
            }
            
            const data = await response.json();
            console.log('GET response data:', data);
            return data;
        } catch (error) {
            console.error('Error in getAllPatients:', error);
            throw error;
        }
    },

    // Add a new patient
    addPatient: async (patientData) => {
        try {
            console.log('Making POST request to:', `${API_URL}/add`);
            console.log('Request body:', patientData);
            
            const response = await fetch(`${API_URL}/add`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: patientData,
            });
            
            console.log('POST response status:', response.status);
            
            if (!response.ok) {
                const errorData = await response.text();
                console.error('POST request failed with status:', response.status);
                console.error('Error response:', errorData);
                throw new Error(`Failed to add patient: ${errorData}`);
            }
            
            const data = await response.json();
            console.log('POST response data:', data);
            return data;
        } catch (error) {
            console.error('Error in addPatient:', error);
            throw error;
        }
    },

    async checkPatientVisit(date) {
        const response = await fetch(`http://localhost:8080/patients/checkVisit/${date}`);
        if (!response.ok) {
            throw new Error('Failed to check patient visit');
        }
        return await response.json();
    },

    async checkPatientVisitByDate(date) {
        const response = await fetch('http://localhost:8080/employee/getPatDate', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ Date: date }),
        });
        if (!response.ok) {
            throw new Error('Failed to check patient visit');
        }
        return await response.json();
    }
}; 