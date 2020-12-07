<template>
  <div id="app">
    <div>
      <label>Researcher Name:</label>
      <input type="text" v-model="name"> <!-- store input into variable name -->
      <button @click = "submitName()">Add</button>


    </div>

  </div>
</template>

<script>

        //namesRef or db
import {namesRef} from './firebase';

export default {
  data () {
    return {
      name : 'Leo' //default value leo
    }
  },
  
  methods: {
    submitName() {
      namesRef.push({name: this.name, sensor: "accelerator, other1, other2"})
    },

  }
}
</script>

