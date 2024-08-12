{ 
description = "Flake to manage a Java 22 workspace.";

inputs.nixpkgs.url = "nixpkgs/nixpkgs-unstable";

outputs = inputs: 
let
  system = "x86_64-linux";
  pkgs = inputs.nixpkgs.legacyPackages.${system};
in { 
  devShell.${system} = pkgs.mkShell rec {
    name = "vector-search";
    buildInputs = with pkgs; [ jdk22 gradle ];
    
    shellHook = ''
      export JAVA_HOME=${pkgs.jdk22}
      PATH="${pkgs.jdk22}/bin:$PATH"
    '';
  };
 };
}
