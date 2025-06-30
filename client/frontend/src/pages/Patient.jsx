import PatientInfo from "./PatientInfo"
import PatientTests from "./PatientTests";
import '../styles/Patient.css'; 


export default function Patient() {
  return (
    <div className="patient-page-layout">
      <section className="patient-info-section">
        <PatientInfo/>
      </section>
      <section className="patient-tests-section">
        <PatientTests/>
      </section>
    </div>
  )
}