import 'bootstrap/dist/css/bootstrap.min.css';

const UpdateImport = (props) => {
  const requestOptions = {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ session: props.session, type: props.ctype, data: props.data })
  };
  const apiUrl = '/SpringBootRepository/import/' + props.session + '/update';

  fetch(apiUrl, requestOptions)
    .then((res) => res.json())
    .then((response) => {
       if (response.status)
            alert("Ok.");
       else
            alert(response.message)
       }
    )
}
export default UpdateImport;

