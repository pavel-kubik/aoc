const calories = input
  .split('\n\n')
  .map(group => group.split('\n')
    .reduce((s, v) => s + parseInt(v), 0));
return Math.max(...calories)