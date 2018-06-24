const HubbingERC721 = artifacts.require('./HubbingERC721.sol');

module.exports = async function(deployer) {
    await deployer.deploy(HubbingERC721, 'HubbingERC721', 'HubbingERC721');
    const erc721 = await HubbingERC721.deployed();
};
