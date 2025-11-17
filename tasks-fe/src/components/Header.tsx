import React from "react";
import { Navbar, NavbarBrand, NavbarContent } from "@nextui-org/react";
import NotificationPanel from "./NotificationPanel";

const Header: React.FC = () => {
  return (
    <Navbar className="border-b">
      <NavbarBrand>
        <h1 className="text-xl font-bold">Task Manager</h1>
      </NavbarBrand>
      <NavbarContent justify="end">
        <NotificationPanel />
      </NavbarContent>
    </Navbar>
  );
};

export default Header;