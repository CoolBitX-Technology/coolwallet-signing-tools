import React from 'react';
import CardInfo from './Setting/cardInfo'
import Wallet from './Wallet/wallet'
import Signing from './Signing/signing'

function SettingManagement({
  appPrivateKey,
  appPublicKey,
  appId,
  transport,
  setAppId,
  isLocked,
  setIsLocked
}) {
  return (
    <>
      <CardInfo transport={transport} isLocked={isLocked} setIsLocked={setIsLocked} />
      <Wallet transport={transport} appId={appId} appPrivateKey={appPrivateKey} isLocked={isLocked} setIsLocked={setIsLocked} />
      <Signing transport={transport} appId={appId} appPrivateKey={appPrivateKey} isLocked={isLocked} setIsLocked={setIsLocked} />
    </>
  )
}
export default SettingManagement
