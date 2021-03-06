import { crypto } from "@coolwallet/core";

export const getAppKeysOrGenerate = (): {
  appPublicKey: string;
  appPrivateKey: string;
} => {
  const appPublicKey = localStorage.getItem("appPublicKey");
  const appPrivateKey = localStorage.getItem("appPrivateKey");
  if (appPublicKey !== null && appPrivateKey !== null) {
    console.log(`Got Keys from localStorage!`);
    return { appPublicKey, appPrivateKey };
  }

  // Generate new keyPair
  const keyPair = crypto.key.generateKeyPair();
  localStorage.setItem("appPublicKey", keyPair.publicKey);
  localStorage.setItem("appPrivateKey", keyPair.privateKey);
  return { appPublicKey: keyPair.publicKey, appPrivateKey: keyPair.privateKey };
};

export const getAppIdOrNull = (): string | null => {
  const appId = localStorage.getItem("appId");
  if (appId === null) {
    console.log("No Appid stored, please register!");
  } else {
    console.log("get AppId success!");
  }
  return appId;
};
