const data = '%data%'.split('\n').map(line => { 
    line = line.replace('tunnel leads to valve ', 'tunnels lead to valves ');
    return {
        valve: line.substring(6, 8),
        rate: Number(line.split('=')[1].split(';')[0]),
        valves: line.split('valves ')[1].split(', '),
        visited: 0
    }
})

let mapValves = new Map()
data.forEach(v => mapValves.set(v.valve, v))
const notNullValves = [...mapValves.values()].filter(v => v.rate > 0)

let max=1000;
let i=0
const processValves = (path, node, time, preasure, maxTime, opened) => {
    if (i++ % 20000000 === 0) {
        console.log('Path ' + path + ' @' + i)
    }
    //const possibleGain = notNullValves.filter(v => !opened.has(v.valve)).reduce((o, v)=>o+v.rate, 0)*(maxTime - time - 1);
    const remainngValves = notNullValves.filter(v => !opened.has(v.valve)).sort().reverse();
    let timeout = maxTime - time - 1;
    const possibleGain = remainngValves.reduce((o, v)=> {
        timeout -= 2
        return o+v.rate*(Math.max(0,timeout+2))
    }, 0);
    if (preasure + possibleGain < max) {
        return preasure
    }
    if (time >= maxTime || opened.size === notNullValves.length) {
        if (preasure > max) {
            max = preasure;
            console.log('New max preasure ' + max + " for path " + path)
        }
        return preasure
    }
    const newPreasure = preasure + node.rate*(Math.max(0,maxTime - time - 1)); // minus open time
    //const visitedValves = node.valves.map(v => mapValves.get(v)).filter(v => visited.has(v.valve))
    //const unvisitedValves = node.valves.map(v => mapValves.get(v)).filter(v => !visited.has(v.valve))
    //const visited = new Set()
    //path.split(',').map(v => v.substring(0, 2)).forEach(v => visited.add(v));
    
    const paths = node.valves.map(v => mapValves.get(v)).sort((a,b) => a.visited-b.visited).map(v => {
        // open and move
        let outPreasure
        if (newPreasure > preasure && !opened.has(node.valve)) {
            // open valve
            node.visited += 1
            opened.add(node.valve);
            const presureOpenA = processValves(path + '+,' + v.valve, v, time + 2, newPreasure, maxTime, opened)
            opened.delete(node.valve);
            const presureOpenB = processValves(path + ',' + v.valve, v, time + 1, preasure, maxTime, opened)
            node.visited -= 1
            outPreasure = Math.max(presureOpenA, presureOpenB)
        } else {
            outPreasure = processValves(path + ',' + v.valve, v, time + 1, preasure, maxTime, opened);
        }
        return outPreasure;
    })
    //console.log('Path ' + path + ' node ' + JSON.stringify(node) + ' @' + time + " paths " + paths)
    return Math.max(...paths);
}

let out = processValves('AA', mapValves.get('AA'), 0, 0, 30, new Set())
console.log('Iterations ' + i)
out
