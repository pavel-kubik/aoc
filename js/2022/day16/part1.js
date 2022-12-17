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

let max=0;
let i=0
const processValves = (path, node, time, preasure, maxTime, opened) => {
    i++;
    const remainngValves = notNullValves.filter(v => !opened.has(v.valve)).sort((a,b)=>b.rate-a.rate);
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

    const pathOpen = []
    if (newPreasure > preasure && !opened.has(node.valve)) {
        // open valve
        opened.add(node.valve);
        pathOpen.push(processValves(path + '+,', node, time + 1, newPreasure, maxTime, opened));
        opened.delete(node.valve);
    }

    const paths = node.valves.map(v => mapValves.get(v)).sort((a,b) => a.visited-b.visited).map(v => {
        return processValves(path + ',' + v.valve, v, time + 1, preasure, maxTime, opened);
    })
    return Math.max(...paths, pathOpen);
}

let out = processValves('AA', mapValves.get('AA'), 0, 0, 30, new Set())
out + ' @Iterations ' + i // 1651 @36314 | 1915 @Iterations 422186
