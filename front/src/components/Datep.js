import React, { useEffect, useState } from 'react';

import "react-datepicker/dist/react-datepicker.css";
import '../styles/datepicker.css'
import 'bootstrap/dist/css/bootstrap.min.css';

import DatePicker from "react-datepicker";

function Datep(props) {
  const [date, setDate] = useState(new Date());
  const handleChange = date => {
    console.log("new date " + date);
    setDate(date);
  }

 useEffect(() => {
    const requestOptions = {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ session: props.session, type: props.ctype, data: date })
    };
    const apiUrl = '/SpringBootRepository/usession';

    fetch(apiUrl, requestOptions)
      .then((res) => res.json())
      .then((response) =>
         console.log("response " + response.message)
      )

  }, [props.session, date]);

  return (
    <div> {props.label}
      <DatePicker className='date-input-field'
        selected={date}
        onChange={ handleChange }
        dateFormat="MM/yyyy"
      />
    </div>
  );
}
export default Datep;

