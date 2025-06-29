import axios from 'axios';

const API_URL = 'https://religious-tammie-tamim21-353bd377.koyeb.app';

export const HematologyService = {
    generateReport: async (reportData) => {
        console.log("reportData", reportData);
        try {
            const response = await axios.post(`${API_URL}/employee/genReport`, reportData, {
                responseType: 'blob'
            });
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    insertTest: async (testData) => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.post(`${API_URL}/doctors/insertest`, JSON.stringify(testData), {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            console.log("response", response);
            return response.data;
        } catch (error) {
            console.error('Error inserting test:', error);
            throw error;
        }
    },

    getTestAttributes: async () => {
        const token = localStorage.getItem('token');
        if (!token) {
            throw new Error('No authentication token found');
        }
        
        try {
            // Decode token to see what's being sent
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));
            const decodedToken = JSON.parse(jsonPayload);
            
            console.log('Attempting to fetch test attributes with token:', token.substring(0, 20) + '...');
            console.log('Full token:', token);
            console.log('Decoded token payload:', decodedToken);
            console.log('User role from token:', decodedToken.role);
            console.log('Request URL:', `${API_URL}/doctors/getatts`);
            
            // First, test if other DOC-authorized endpoints work
            console.log('Testing other DOC-authorized endpoint...');
            try {
                const testResponse = await axios.post(`${API_URL}/doctors/getDocPatients`, null, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });
                console.log('Other DOC endpoint works:', testResponse.status);
            } catch (testError) {
                console.error('Other DOC endpoint also fails:', testError.response?.status);
            }
            
            const response = await axios.get(`${API_URL}/doctors/getatts`, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
            console.log("Test attributes response:", response);
            return response.data;
        } catch (error) {
            console.error('Error fetching test attributes:', error);
            console.error('Error response:', error.response);
            console.error('Error status:', error.response?.status);
            console.error('Error data:', error.response?.data);
            console.error('Error headers:', error.response?.headers);
            
            // Try with different role formats
            if (error.response?.status === 403) {
                console.log('Trying with different role formats...');
                
                // Try with lowercase role
                try {
                    const modifiedToken = token.replace('"role":"DOC"', '"role":"doc"');
                    console.log('Trying with lowercase role...');
                    const response = await axios.get(`${API_URL}/doctors/getatts`, {
                        headers: {
                            'Authorization': `Bearer ${modifiedToken}`,
                            'Content-Type': 'application/json'
                        }
                    });
                    console.log("Lowercase role response:", response);
                    return response.data;
                } catch (lowercaseError) {
                    console.error('Lowercase role also failed:', lowercaseError.response?.status);
                }
                
                // Try without Content-Type header
                try {
                    console.log('Trying without Content-Type header...');
                    const response = await axios.get(`${API_URL}/doctors/getatts`, {
                        headers: {
                            'Authorization': `Bearer ${token}`
                        }
                    });
                    console.log("No Content-Type response:", response);
                    return response.data;
                } catch (noContentTypeError) {
                    console.error('No Content-Type also failed:', noContentTypeError.response?.status);
                }
            }
            
            if (error.response?.status === 403) {
                throw new Error('Access denied. You may not have the required permissions to access test attributes.');
            }
            throw error;
        }
    },

    parseHtmlFile: async (file) => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = (event) => {
                try {
                    const htmlContent = event.target.result;
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(htmlContent, 'text/html');
                    
                    const extractedData = {
                        patient_info: {
                            name: doc.querySelector('.patient-name')?.textContent || '',
                            age: parseInt(doc.querySelector('.patient-age')?.textContent) || 0,
                            gender: doc.querySelector('.patient-gender')?.textContent || '',
                            test_id: doc.querySelector('.test-id')?.textContent || ''
                        },
                        test_details: {
                            sample_date: doc.querySelector('.sample-date')?.textContent || '',
                            print_date: new Date().toISOString().split('T')[0],
                            referring_physician: doc.querySelector('.physician')?.textContent || ''
                        },
                        blood_count_report: {
                            tests: Array.from(doc.querySelectorAll('.test-row')).map(row => ({
                                test_name: row.querySelector('.test-name')?.textContent || '',
                                result: parseFloat(row.querySelector('.test-result')?.textContent) || 0,
                                unit: row.querySelector('.test-unit')?.textContent || '',
                                reference_range: row.querySelector('.reference-range')?.textContent || '',
                                flag: row.querySelector('.test-flag')?.textContent || ''
                            })),
                            report_comments: doc.querySelector('.report-comments')?.textContent || ''
                        }
                    };
                    resolve(extractedData);
                } catch (error) {
                    reject(error);
                }
            };
            reader.onerror = (error) => reject(error);
            reader.readAsText(file);
        });
    }
}; 