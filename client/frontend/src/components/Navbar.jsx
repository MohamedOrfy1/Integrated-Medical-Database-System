import { Link } from 'react-router-dom';

export default function Navbar() {
    return (
        <nav>
            <ul>
                <li><Link to="/">Home</Link></li>
                <li><Link to="/receptionist">Receptionist</Link></li>
                <li><Link to="/department">Department</Link></li>
                <li><Link to="/login">Login</Link></li>
                <li><Link to="/signup">Signup</Link></li>
            </ul>
        </nav>
    )
}
