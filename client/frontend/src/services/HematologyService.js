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
            console.log('Attempting to fetch test attributes with token:', token.substring(0, 20) + '...');
            console.log('Full token:', token);
            console.log('Request URL:', `${API_URL}/doctors/getatts`);
            
            // Test if the endpoint is accessible
            console.log('Testing endpoint accessibility...');
            
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
            
            // Try alternative endpoints if the main one fails
            if (error.response?.status === 403 || error.response?.status === 404) {
                console.log('Trying alternative endpoints...');
                
                // Try without /doctors prefix
                try {
                    const altResponse = await axios.get(`${API_URL}/getatts`, {
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json'
                        }
                    });
                    console.log("Alternative endpoint response:", altResponse);
                    return altResponse.data;
                } catch (altError) {
                    console.error('Alternative endpoint also failed:', altError);
                }
                
                // Try with /employee prefix
                try {
                    const empResponse = await axios.get(`${API_URL}/employee/getatts`, {
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json'
                        }
                    });
                    console.log("Employee endpoint response:", empResponse);
                    return empResponse.data;
                } catch (empError) {
                    console.error('Employee endpoint also failed:', empError);
                }
                
                // Try POST method instead of GET
                try {
                    const postResponse = await axios.post(`${API_URL}/doctors/getatts`, {}, {
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json'
                        }
                    });
                    console.log("POST method response:", postResponse);
                    return postResponse.data;
                } catch (postError) {
                    console.error('POST method also failed:', postError);
                }
                
                // Try different endpoint names
                const alternativeEndpoints = [
                    '/doctors/getattributes',
                    '/doctors/gettestattributes', 
                    '/doctors/referenceranges',
                    '/doctors/tests',
                    '/employee/getattributes',
                    '/employee/gettestattributes'
                ];
                
                for (const endpoint of alternativeEndpoints) {
                    try {
                        console.log(`Trying endpoint: ${endpoint}`);
                        const altResponse = await axios.get(`${API_URL}${endpoint}`, {
                            headers: {
                                'Authorization': `Bearer ${token}`,
                                'Content-Type': 'application/json'
                            }
                        });
                        console.log(`Success with endpoint ${endpoint}:`, altResponse);
                        return altResponse.data;
                    } catch (altError) {
                        console.error(`Endpoint ${endpoint} failed:`, altError.response?.status);
                    }
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