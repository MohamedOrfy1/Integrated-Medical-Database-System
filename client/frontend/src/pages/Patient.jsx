import PatientInfo from "./PatientInfo"
import PatientTests from "./PatientTests";
import '../styles/Patient.css'; 


export default function Patient() {
  return (
    <div className="patient-container">
      <div className="patient-info-section">
        <PatientInfo/>
      </div>
      
      <div className="patient-visits-section">
        <PatientTests/>
      </div>
    </div>
  )
}