import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: JSON.parse(localStorage.getItem('airQualityUser') || 'null')
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
      if (user) {
        localStorage.setItem('airQualityUser', JSON.stringify(user))
      } else {
        localStorage.removeItem('airQualityUser')
      }
    }
  },
  actions: {
    logout({ commit }) {
      commit('SET_USER', null)
    }
  },
  getters: {
    isLoggedIn: state => !!state.user,
    isAdmin: state => state.user && state.user.role === 1
  }
})
