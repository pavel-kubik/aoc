const input = '%data%'.split('\n').map(line => {
    const items = line.split(':')[1].split('.');
    const ore = items[0].match(/\d+/g).map(Number);
    const clay = items[1].match(/\d+/g).map(Number);
    const obsidian = items[2].match(/\d+/g).map(Number);
    const geode = items[3].match(/\d+/g).map(Number);
    return [
        [ore[0], 0, 0, 0],
        [clay[0], 0, 0, 0],
        [obsidian[0], obsidian[1], 0, 0],
        [geode[0], 0, geode[1], 0]
    ];
});

const START=[1,0,0,0]

const canBuildBot = (botIdx, sources, bp) => {
    const botCosts = bp[botIdx];
    return botCosts.every((cost,i) => cost <= sources[i])
}

const buildBot = (botIdx, sources, bp) => {
    const botCosts = bp[botIdx];
    return sources.map((c,i) => c -= botCosts[i])
}

const add = (a, b) => a.map((c,i) => c + b[i])
const remove = (a, b) => a.map((c,i) => c - b[i])
const mul = (a, b) => a.map((c,i) => c * b[i])

const inc = (a, idx) => a.map((v, i) => i === idx ? v + 1 : v)
const dec = (a, idx) => a.map((v, i) => i === idx ? v - 1 : v)

const estimateBest = (sources, bots, left, bp) => {
    return sources[3] + (bots[3]+left-1)*left;
}

const MAX_ITERATION_PER_BP = 10000000;

const maxRobots = (bp) => [
    Math.max(...bp.map(l=>l[0])),
    Math.max(...bp.map(l=>l[1])),
    Math.max(...bp.map(l=>l[2])),
]

const evaluateStep = (initBots, initLeft, bp) => {
    let i = 0;
    let max = 0;
    let cut = [0,0,0]

    let hashes = {}
    let botHashes = {}
    let sourceHashes = {}

    let queue = [];
    queue.push({
        bots: initBots,
        sources: [0,0,0,0],
        build: [0,0,0,0],
        left: initLeft
    })

    const maxBots = maxRobots(bp);

    while(queue.length > 0) {
        queue.sort((a,b)=> {
            if (a.left === b.left) {
                if (b.sources[3] === a.sources[3]) {
                    return b.sources[2] - a.sources[2]
                }
                return b.sources[3] - a.sources[3]
            }
            return a.left - b.left;
        })
        let s = queue.shift();  //pop();
        if (i > MAX_ITERATION_PER_BP) { // stop after max iteration
            //return max;
        }
        // stop condition
        if (s.left === 0) {
            if (max < s.sources[3]) {
                max = s.sources[3];
                console.log("T " + s.left + " " + s.bots + " " + s.sources + " MAX " + max + ' @' + i);
            }
            continue;
        }
        // hash position speed up
        // const hash = 'T'+s.left+'B'+s.bots.join(':')+'S'+s.sources.join(':');
        // if (hashes[hash]) {
        //     cut[0]++;
        //     continue;
        // }
        // hash bot speed up
        const botHash = 'T'+s.left+'B'+s.bots.join(':');
        if (botHashes[botHash]) {
            const hs = botHashes[botHash];
            if (s.sources.every((src,i) => src <= hs[i])) {
                cut[1]++;
                continue;
            } else if (s.sources.every((src,i) => src > hs[i])) {
                 botHashes[botHash] = s.sources.slice();
            }
        } else {
            botHashes[botHash] = s.sources.slice();
        }
        const sourceHash = 'T'+s.left+'B'+s.sources.join(':');
        if (sourceHashes[sourceHash]) {
            const hb = sourceHashes[sourceHash];
            if (s.bots.every((b,i) => b <= hb[i])) {
                cut[1]++;
                continue;
            } else if (s.bots.every((b,i) => b > hb[i])) {
                 sourceHashes[sourceHash] = s.bots.slice();
            }
        } else {
            sourceHashes[sourceHash] = s.bots.slice();
        }
        // cut condition
        const estimate = estimateBest(s.sources, s.bots, s.left, bp);
        if (estimate < max) {
            cut[2]++;
            continue;
        }
        // collect resources
        const sources = add(s.sources, s.bots);
        // build bots
        const bots = add(s.bots, s.build);
        if (++i % 10000 === 0) {
            console.log("Step " + i + ' cut ' + cut.join(':') + ' queue ' + queue.length + ' MAX ' + max);
        }
        [3,2,1,0].forEach(i => {
            if (i < 3 && s.bots[i] >= maxBots[i]) {
                cut[0]++
                return
            }
            if (canBuildBot(i, sources, bp)) {
                const botPrice = bp[i];
                queue.push({
                    bots: bots,
                    sources: remove(sources, botPrice),
                    build: inc([0,0,0,0], i),
                    left: s.left - 1,
                });
            }
        });
        queue.push({
                    bots: bots,
                    sources: sources,
                    build: [0,0,0,0],
                    left: s.left - 1,
                });
    }
    console.log('Evaluated after steps: ' + i + ' MAX ' + max);
    return max;
}

const evaluateBP = (bp, time) => {
    return evaluateStep(START, time, bp);
}

let output = 0
input.forEach((bluprint, i) => {
    const best = evaluateBP(bluprint, 24);
    output += (i+1)*best;
    console.log('Blueprint ' + (i+1) + ' found with ' + best);
})

//let output = evaluateBP(input[1], 24)
/*
without cut
- T 1 1,6,2,2 2,47,8,9 MAX 9   @3806443   [+endless search of rest]

with cut `sources[3] + (bots[3]+left)*left`
- T 1 1,6,2,2 2,47,8,9 MAX 9    @302079   20000000+ DNF

with cut `sources[3] + (bots[3]+left-1)*left`
- T 1 1,6,2,2 2,47,8,9 MAX 9     @97446   20000000+ DNF

with cut 
- T 1 1,6,2,2 2,47,8,9 MAX 9     @35507     1201340 steps
- 2nd                MAX 12    @5549615     7372925 steps

with cut hash positions and hash bots
- T 1 1,6,2,2 2,47,8,9 MAX 9     @35469     1198435 steps
- 2nd                MAX 12    @5549615     7372925 steps


*/

// 1201 too low