//
//  ContentView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 25.10.2023.
//

import SwiftUI

struct ContentView: View {
    var body: some View {
        Button("Добавить мастера") {
            let newMaster = Master(name: "Иван Иванович")
            NetworkManager.shared.addMaster(master: newMaster) { success in
                if success {
                    print("Мастер успешно добавлен")
                } else {
                    print("Ошибка при добавлении мастера")
                }
            }
        }
    }
}

