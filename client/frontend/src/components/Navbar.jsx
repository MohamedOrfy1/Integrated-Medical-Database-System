import { Link } from 'react-router-dom';
import '../styles/Navbar.css';

export default function Navbar() {
    return (
        <nav className="nav-container">
            <div className="nav-content">
                <div className="nav-left">
                    <ul className="nav-links">
                        <li><Link to="/">Home</Link></li>
                        <li><Link to="/about">About</Link></li>
                    </ul>
                </div>

                <div className="nav-auth">
                    <Link to="/login" className="nav-button">Login</Link>
                    <Link to="/signup" className="nav-button">Sign Up</Link>
                </div>
            </div>
        </nav>
    );
}
