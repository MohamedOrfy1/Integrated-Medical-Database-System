import PatientCard from "./PatientCard.Jsx"
import PatientVisits from "./PatientVisits"

export default function Patient() {
  return <>

  <div className="flex flex-col lg:flex-row gap-6 bg-gray-100 p-6">
    <div className="w-full lg:w-1/2">
      <PatientCard/>
    </div>
    
    <div className="w-full lg:w-1/2">
      <PatientVisits/>
    </div>
  </div>
    </>

}