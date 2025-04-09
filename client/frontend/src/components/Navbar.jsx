import { Link } from 'react-router-dom';
import '../styles/Navbar.css';

const Navbar = () => {
  return (
    <nav className="navbar">
      <div className="nav-brand">
        <Link to="/">MRI System</Link>
      </div>
      <div className="nav-links">
        <Link to="/">Home</Link>
        <Link to="/receptionist">Receptionist</Link>
        <Link to="/department">Department</Link>
        <Link to="/login">Login</Link>
        <Link to="/signup">Signup</Link>
      </div>
    </nav>
  );
};

export default Navbar; 