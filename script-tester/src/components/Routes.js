import React from "react";
import { HashRouter as Switch, Route, Redirect } from "react-router-dom";
import { useState } from 'react';
import { getAppIdOrNull } from "../utils/keypairUtil";
import Setting from ".";

function Routes({ appPublicKey, appPrivateKey, transport, isLocked, setIsLocked }) {
  const [appId, setAppId] = useState(getAppIdOrNull());
  return (
    <Switch>
      <Redirect from='/' to='/setting' />
      <Route
        path="/setting/"
        children={
          <Setting
            transport={transport}
            appPrivateKey={appPrivateKey}
            appPublicKey={appPublicKey}
            appId={appId}
            setAppId={setAppId}
            isLocked={isLocked}
            setIsLocked={setIsLocked}
          />
        }
        appId={appId}
      />
    </Switch>

  );
}

export default Routes;
