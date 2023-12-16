//
//  AddCarView.swift
//  AutoServiceERP-Frontend
//
//  Created by Александр Сафронов on 01.12.2023.
//


import SwiftUI

struct AddCarView: View {
    @Binding var cars: [Car]
    @State private var num = ""
    @State private var color = ""
    @State private var mark = ""
    @State private var foreign = false
    @State private var showErrorAlert = false
    @State private var errorMessage = ""
    @Environment(\.presentationMode) var presentationMode

    var body: some View {
        NavigationView {
            Form {
                TextField("Номер", text: $num)
                TextField("Цвет", text: $color)
                TextField("Марка", text: $mark)
                Toggle("Импортный", isOn: $foreign)
                Button("Добавить") {
                    addCar()
                }
            }
            .navigationTitle("Новый автомобиль")
            .alert(isPresented: $showErrorAlert) {
                Alert(title: Text("Ошибка"), message: Text(errorMessage), dismissButton: .default(Text("OK")))
            }
        }
    }

    func addCar() {
        let newCar = Car(num: num, color: color, mark: mark, foreign: foreign)
        CarNetworkManager.shared.addCar(car: newCar) { success in
            if success {
                cars.append(newCar)
                presentationMode.wrappedValue.dismiss()
            } else {
                // Обработка ошибок
                errorMessage = "Автомобиль с номером \(newCar.num) уже существует"
                showErrorAlert = true
            }
        }
    }
}
