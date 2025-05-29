import axios from 'axios';

const API_URL = "https://religious-tammie-tamim21-353bd377.koyeb.app/";

// Configure axios defaults
axios.defaults.withCredentials = true;

class AuthService {
    async login(username, password, role) {
        try {
            let endpoint = '';
            switch(role) {
                case 'DOC':
                    endpoint = 'doctors/login';
                    break;
                case 'EMP':
                    endpoint = 'employee/login';
                    break;
                default:
                    throw new Error('Invalid role');
            }

            const response = await axios.post(API_URL + endpoint, {
                username,
                password
            }, {
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                withCredentials: true
            });

            if (response.data === "false") {
                throw new Error('Invalid credentials');
            }

            // Store the JWT token
            localStorage.setItem("token", response.data);
            localStorage.setItem("role", role);
            return {
                accessToken: response.data,
                role: role
            };
        } catch (error) {
            console.error('Login error:', error);
            throw error;
        }
    }

    logout() {
        localStorage.removeItem("token");
        localStorage.removeItem("role");
    }

    register(username, email, password, role) {
        return axios.post(API_URL + "signup", {
            username,
            email,
            password,
            role
        }, {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            withCredentials: true
        });
    }

    getCurrentUser() {
        const token = localStorage.getItem('token');
        const role = localStorage.getItem('role');
        return token ? { accessToken: token, role: role } : null;
    }

    isAuthenticated() {
        return !!localStorage.getItem('token');
    }

    getRole() {
        return localStorage.getItem('role');
    }
}

export default new AuthService(); 