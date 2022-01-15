import 'bootstrap/dist/css/bootstrap.min.css';

const Updateu = (props) => {
  const requestOptions = {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ session: props.session, type: props.ctype, data: props.data })
  };
  const apiUrl = '/SpringBootRepository/usession';

  fetch(apiUrl, requestOptions)
    .then((res) => res.json())
    .then((response) => {
         alert(response.message);
         console.log("response " + response.message);
       }
    )
}
export default Updateu;

