import PatientInfo from "./PatientInfo"
import PatientVisits from "./PatientVisits"



export default function Patient() {
  console.log("hiiiiiiii")
  return <>
  <div className="flex flex-col lg:flex-row gap-6 bg-gray-100 p-6">
    <div className="w-full lg:w-1/2">
      <PatientInfo/>
    </div>
    
    <div className="w-full lg:w-1/2">
      <PatientVisits/>
    </div>
  </div>
  </>

}