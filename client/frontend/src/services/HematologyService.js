import axios from 'axios';
import { CBC_TESTS } from './cbcTestMeta';

const API_URL = 'https://religious-tammie-tamim21-353bd377.koyeb.app';

export const HematologyService = {
    generateReport: async (reportData) => {
        console.log("reportData", reportData);
        try {
            const response = await axios.post(`${API_URL}/doctors/genReport`, reportData, {
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
        // Ignore the endpoint, always return mapped CBC_TESTS
        return CBC_TESTS.map(test => ({
            attributeName: test.name,
            unit: test.unit || '',
            fromRange: typeof test.min !== 'undefined' ? test.min : '',
            toRange: typeof test.max !== 'undefined' ? test.max : ''
        }));
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