import React, { useState } from "react";
import { Navbar, Nav } from "react-bootstrap";
import "./NavBar.css";

function MyNavBar() {
  const [activeTab, setActiveTab] = useState("#setting");

  return (
    <Navbar variant="dark" expand="lg">
      <Nav
        variant="tabs"
        activeKey={activeTab}
        onSelect={(key) => {
          console.log("key :", key);
          setActiveTab(key);
        }}
      >
        <Nav.Item>
          <Nav.Link className="NavItem" href="#setting">
            Setting
          </Nav.Link>
        </Nav.Item>
      </Nav>
    </Navbar>
  );
}

export default MyNavBar;
