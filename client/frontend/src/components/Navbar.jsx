import { Link, useNavigate } from 'react-router-dom';
import '../styles/Navbar.css';

export default function Navbar() {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');
    const userRole = token ? JSON.parse(atob(token.split('.')[1])).role : null;

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    return (
        <nav className="nav-container">
            <div className="nav-content">
                <div className="nav-left">
                    <ul className="nav-links">
                        <li><Link to="/">Home</Link></li>
                        <li><Link to="/about">About</Link></li>
                        {userRole === 'DOC' && (
                            <li><Link to="/doctor-dashboard">Doctor Dashboard</Link></li>
                        )}
                        {userRole === 'DOC' && (
                            <li><Link to="/hematology">Hematology</Link></li>
                        )}
                    </ul>
                </div>

                <div className="nav-auth">
                    {!token ? (
                        <>
                            <Link to="/login" className="nav-button">Login</Link>
                            {/* <Link to="/signup" className="nav-button">Sign Up</Link> */}
                        </>
                    ) : (
                        <button className="logout-button" onClick={handleLogout}>Logout</button>
                    )}
                </div>
            </div>
        </nav>
    );
}
