//
//  ContentView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 25.10.2023.
//

import SwiftUI

struct ContentView: View {
    @State private var inputText: String = ""
    
    var body: some View {
        TextField("Введите текст...", text: $inputText)
            .padding()
            .textFieldStyle(RoundedBorderTextFieldStyle())
    }
}
