import React, { useContext, useEffect, useState } from "react";
import axios from "axios";
import { PatientContext } from '../Context/PatientContext';

export default function PatientCard(){
    const patient = {
    first_name: "Cameron",
    family_name: "Williamson",
    registrationDate: "12.05.2019",
    age: 37,
  };  
  //const[patient,setPatient]=useState(null);
    let{patientId}=useContext(PatientContext)
    function getPatientInfo(){
        axios.post(`https://religious-tammie-tamim21-353bd377.koyeb.app/getPatient`,{
            "PatientID": patientId
        })
        .then((response)=>{
            console.log(response)
            //set patients to the response from the api response
        })
        .catch(()=>{

        })
    }
    useEffect(()=>{
        getPatientInfo()
    },[])

  return <>
      <div className="bg-white rounded-2xl p-6 max-w-xl mx-auto shadow text-gray-900 space-y-6">
      <div className="text-xl font-bold text-blue-400">{patient?.first_name} {patient?.family_name}
      </div>

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
};





