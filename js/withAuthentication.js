import React from 'react'
import { connect } from 'react-redux'
import { compose } from 'redux'

import { withFirebase } from '../services'
import { setUser } from '../actions'

const withAuthentication = Component => {
  class WithAuthentication extends React.Component {
    constructor(props) {
      super(props);
      this.props.onSetUser(
        JSON.parse(localStorage.getItem('authUser'))
      )
    }

    componentDidMount() {
      this.listener = this.props.firebase.onAuthUser(
        authUser => {
          localStorage.setItem('authUser', JSON.stringify(authUser));
          this.props.onSetUser(authUser)
        },
        () => {
          localStorage.removeItem('authUser');
          this.props.onSetUser(null)
        }
      )
    }

    componentWillUnmount() {
      this.listener()
    }

    render() {
      return <Component {...this.props} />
    }
  }

  const mapDispatchToProps = dispatch => ({
    onSetUser: authUser => dispatch(setUser(authUser))
  });

  return compose(
    withFirebase,
    connect(null, mapDispatchToProps)
  )(WithAuthentication)
};

export default withAuthentication
