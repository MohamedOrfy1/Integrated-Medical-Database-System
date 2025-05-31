import { createContext, useState } from "react";
export let PatientContext=createContext();

export default function PatientInfoProvider(props){
    const[patientId,setPatientId]=useState(null)
    
    return <PatientContext.Provider value={{patientId,setPatientId}}>
      {props.children}

    </PatientContext.Provider>

}