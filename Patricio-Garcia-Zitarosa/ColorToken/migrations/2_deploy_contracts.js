const ColorToken = artifacts.require('./ColorToken.sol');

module.exports = function(deployer) {
    deployer.deploy(ColorToken);
};
