import React, { Component } from 'react';

class Session extends Component {

  render() {
    function message() {
      alert('New Session!');
    }

    return (
      <button onClick={message}>New Session</button>
    );
  }
}

export default Session;