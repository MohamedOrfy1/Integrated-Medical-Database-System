import React, { useContext, useEffect, useState } from "react";
import axios from "axios";
import { PatientContext } from "../Context/PatientContext";

export default function PatientCard(){
    const[patient,setPatient]=useState(null);
    let{patientId}=useContext(PatientContext)
    function getPatientInfo(){
        axios.post(`https://religious-tammie-tamim21-353bd377.koyeb.app/getPatient`,{
            "PatientID": patientId
        })
        .then((response)=>{
            console.log(response)
            
        })
        .catch(()=>{

        })
    }
    useEffect(()=>{
        getPatientInfo()
    },[])

  return <>
      <div className="bg-white rounded-2xl p-6 max-w-xl mx-auto shadow text-gray-900 space-y-6">
      <div className="text-xl font-bold">{patient?.first_name} {patient?.family_name}</div>

      <div className="grid grid-cols-2 gap-y-4 gap-x-6 text-sm">
        <div>
          <p className="text-gray-500">Registration Date</p>
          <p>{patient?.registrationDate}</p>
        </div>
        <div>
          <p className="text-gray-500">Age</p>
          <p>{patient?.age}</p>
        </div>
      </div>
  </div>
  </>
//   (
//     <div className="bg-white rounded-2xl p-6 max-w-xl mx-auto shadow text-gray-900">
//       {/* Header Section */}
//       <div className="flex justify-between items-start mb-6">
//         <div>
//           <p className="text-sm text-gray-500">Name</p>
//           <h2 className="text-lg font-semibold">{patient.name}</h2>
//         </div>
//       </div>

//       {/* Details Grid */}
//       <div className="grid grid-cols-2 gap-y-4 gap-x-6 text-sm">
//         <div>
//           <p className="text-gray-500">Birth date</p>
//           <p>{patient.birth_date}</p>
//         </div>
//         <div>
//           <p className="text-gray-500">Gender</p>
//           <p>{patient.gender}</p>
//         </div>

//         <div>
//           <p className="text-gray-500">Age</p>
//           <p>{patient.age}</p>
//         </div>

//         <div>
//           <p className="text-gray-500">Phone</p>
//           <div className="flex items-center gap-2">
//             <Phone className="w-4 h-4 text-gray-600" />
//             <span>{patient.phone}</span>
//           </div>
//         </div>

//         <div className="col-span-2">
//           <p className="text-gray-500">Address</p>
//           <div className="flex items-start gap-2 mt-1">
//             <Home className="w-4 h-4 text-gray-600 mt-1" />
//             <span>{patient.address}</span>
//           </div>
//         </div>
//       </div>
//     </div>
//   );

};


