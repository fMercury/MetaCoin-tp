var MTG = artifacts.require('./MTG.sol');

var hashes = [
    'QmZHGGS78kUWhbAQHKr2wPYqFLZGLnF7iBQuaJVRP7BU2u',
    'QmQ1zSuNCXx5k7p79bb19c2wXgJzbc5LUsJgRWKZNHtF92',
    'QmUfzjDq8V9wmVyTf3yyn4DFLX8swPJh4Dsz6zvg8QkmBT',
    'QmXDv5aVtjdyrTkZMW8eXSNBUS64yFWQgd4kncmFJtm2VY',
    'QmfZX6ybvc3frwSBJzrQr5ceZCHf2c8einePjaRNggS5WW',
    'QmXrDFKyCwzQQoeeySRFyKeSLQDsZBDk6z6DymjQVaJumW',
    'QmSYTK2Rk4BFmzRw3Mv9hd9q8XRYd4rhHWeE7EfGXABpoR',
    'QmNT2Vp4Rj7ysXRp19YkUDbH4BUF1MvG6RbJu7BcVCsn2P',
    'QmaVJkxbMZ8vxEKQAjnmydRpKzywJAAbF5ys8R2RyjtSZb',
    'QmauVVjuAihxmBaWXhmB7uEnAVbLwq7qKhVUz2uafcpR1D',
    'QmaJmzqteDHV8cFzGEjvUo8veyxDfPn5pHw57MAXE2mLXL'
];

module.exports = function(deployer) {
    deployer.deploy(MTG).then(contract => {
        hashes.forEach(function(hash) {
            contract.mint(hash, web3.toWei(0.001, 'ether'));
        });
    });
};
