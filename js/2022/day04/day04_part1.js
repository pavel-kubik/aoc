let sum = 0;
input.trim().split('\n').forEach(line => {
  let [from, to] = line.split(',');
  let [a, b] = from.split('-').map(Number);
  let [c, d] = to.split('-').map(Number);
  sum += (((a <= c && b >=d) || (a >= c && b <= d)) ? 1 : 0)
});
return sum;