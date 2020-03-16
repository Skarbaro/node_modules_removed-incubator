import React from 'react'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import { compose } from 'redux'

import { withFirebase } from '../services'

const withAuthorization = condition => Component => {
  class WithAuthorization extends React.Component {
    componentDidMount() {
      this.listener = this.props.firebase.onAuthUser(
        authUser => {
          if (!condition(authUser)) {
            this.props.history.push('/sign_in')
          }
        },
        () => this.props.history.push('/sign_in')
      )
    }

    componentWillUnmount() {
      this.listener()
    }

    render() {
      return condition(this.props.user) ? (
        <Component {...this.props} />
      ) : null
    }
  }

  const mapStateToProps = state => ({
    user: state.userState
  });

  return compose(
    withRouter,
    withFirebase,
    connect(mapStateToProps)
  )(WithAuthorization)
};

export default withAuthorization
